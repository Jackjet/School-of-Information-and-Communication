package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.util.AlgorithmHelper;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.CacheManger;
import d1.framework.webapi.utils.SignInRetryLimitManager;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.business.dao.StatisticActiveUserDao;
import d1.project.caict.business.dao.UserDao;
import d1.project.caict.business.entity.StatisticActiveUser;
import d1.project.caict.business.entity.Users;
import d1.project.caict.business.model.MessageContentInsertVm;
import d1.project.caict.business.model.UserFindAllVm;
import d1.project.caict.business.model.UserUpdateVm;
import d1.project.caict.business.utils.Constants;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.service.IOperationLogService;
import d1.project.caict.common.utils.BaseUtils;
import net.dcrun.component.ehcache.IEhcacheService;
import net.dcrun.component.sms.ucpass.ISmsUcpassService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class UserService {

    private final UserDao userDao;
    private final StatisticActiveUserDao statisticActiveUserDao;
    private final ISmsUcpassService smsUcpassService;
    private final IEhcacheService cache;
    private final IOperationLogService iOperationLogService;
    private final MessageService messageService;

    private final static String authType = "web";

    @Value("${sms.ucpaas.login.code}")
    private String loginSmsTemplateCode;

    @Value("${sms.ucpaas.update-password.code}")
    private String updatePasswordSmsTemplateCode;

    public UserService(UserDao userDao,
                       StatisticActiveUserDao statisticActiveUserDao,
                       ISmsUcpassService smsUcpassService,
                       IOperationLogService operationLogService,
                       MessageService messageService) {

        this.userDao = userDao;
        this.statisticActiveUserDao = statisticActiveUserDao;
        this.smsUcpassService = smsUcpassService;
        this.cache = CacheManger.getInstance().getCache();
        this.iOperationLogService = operationLogService;
        this.messageService = messageService;
    }

    public Page<Users> findAll(UserFindAllVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<Users> builder = new SpecificationBuilder<>();
        return userDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }

    public Optional<Users> find(String id) {
        return userDao.findById(id);
    }

    public void resetPwd(String id, String password, String plaintext, HttpServletRequest request) throws DoValidException {
        Users user = this.userDao.findById(id).orElseThrow(() -> new DoValidException("用户信息不存在"));
        user.setPassword(password);
        user.setUpdateTime(Calendar.getInstance());
        user.setUpdateById(Utils.getCurrentUserId(request));
        this.userDao.save(user);

        List<String> receiverIds = new ArrayList<>();
        receiverIds.add(id);
        //插入一条消息
        MessageContentInsertVm model = new MessageContentInsertVm();
        model.setSenderId("admin");
        model.setTitle("重置密码");
        model.setText("管理员将你的密码重置，重置后密码为：" + plaintext + ", 请尽快修改密码！");
        model.setUserIds(receiverIds);
        messageService.send(model);
    }

    /**
     * 发送验证码
     *
     * @param type  验证码类型：0 登录验证码 、1 修改密码验证码
     * @param phone 手机号
     * @throws Exception 异常
     */
    public void sendVerificationCode(int type, String phone) throws Exception {
        String verifyCode = String.valueOf(new Random().nextInt(899999) + 100000);
        String smsTemplateCode;

        switch (type) {
            case 0:
                smsTemplateCode = loginSmsTemplateCode;
                break;
            case 1:
                smsTemplateCode = updatePasswordSmsTemplateCode;
                break;
            default:
                throw new Exception("不支持的短信类型!");
        }
        smsUcpassService.send(verifyCode, phone, smsTemplateCode);

        //验证码存入缓存，10分钟有效
        cache.setExpireString(phone + "_" + type, verifyCode, Constants.VER_CODE_EXPIRE_TIME);
    }

    public SignInResult loginWithPhone(String phone, String code) throws DoValidException, UnsupportedEncodingException {
        //判断验证码是否合法
        if (!cache.containsKey(phone + "_0")) {
            throw new DoValidException("手机验证码已经失效,请重新发送");
        }

        String verCode = cache.getString(phone + "_0");
        if (!verCode.equals(code)) {
            throw new DoValidException("手机验证码输入错误");
        }
        Users entity;
        //判断手机号是否注册
        Optional<Users> entityOpt = userDao.findByPhone(phone);

        //如果不存在就去注册下
        if (entityOpt == null || !entityOpt.isPresent()) {
            entity = save(phone);
        } else {
            entity = entityOpt.get();
        }
        return getToken(entity);
    }

    public SignInResult loginWithPassword(String phoneOrUsername, String password) throws DoValidException {
        if (SignInRetryLimitManager.getInstance().verifyIsLocked(phoneOrUsername)) {
            throw new DoValidException("用户重试错误密码多次，导致用户被锁");
        }

        Users entity = findByNameOrPhone(phoneOrUsername).orElseThrow(() -> new DoValidException("用户不存在，请用手机号验证码登录!"));
        if (!entity.getPassword().equals(password)) {
            SignInRetryLimitManager.getInstance().signInWithWrongPwd(phoneOrUsername);
            throw new DoValidException("密码输入错误");
        }
        SignInRetryLimitManager.getInstance().signInSuccess(phoneOrUsername);
        return getToken(entity);
    }

    private SignInResult getToken(Users entity) {
        entity.setLastSignInTime(Calendar.getInstance());
        this.userDao.save(entity);

        StatisticActiveUser statisticActiveUser = new StatisticActiveUser();
        statisticActiveUser.setId(BaseUtils.generate32Id());
        statisticActiveUser.setUserId(entity.getId());
        statisticActiveUser.setVisitDate(new SimpleDateFormat("yyyy-MM-dd").format(entity.getLastSignInTime().getTime()));

        this.statisticActiveUserDao.save(statisticActiveUser);

        String token = TokenManager.getInstance().createToken(authType, (JSONObject) JSON.toJSON(entity), Constants.TOKEN_EXPIRE_TIME);
        return new SignInResult(entity.getId(), entity.getUsername(), token);
    }


    public void delete(String id) {
        this.userDao.deleteById(id);
    }

    public void singOut(HttpServletRequest request) {
        TokenManager.getInstance().removeToken(request);
    }

    public void updatePassword(String phone, String code, String password, HttpServletRequest request) throws Exception {
        //判断验证码是否合法
        if (!cache.containsKey(phone + "_1")) {
            throw new Exception("手机验证码已经失效,请重新发送");
        }

        String verCode = cache.getString(phone + "_1");
        if (!verCode.equals(code)) {
            throw new Exception("手机验证码输入错误");
        }

        Users entity = this.userDao.findByPhone(phone).orElseThrow(() -> new DoValidException("手机号：" + phone + " 尚未注册"));
        entity.setPassword(password);
        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        this.userDao.save(entity);

        List<String> receiverIds = new ArrayList<>();
        receiverIds.add(Utils.getCurrentUserId(request));

        //插入一条消息
        MessageContentInsertVm model = new MessageContentInsertVm();
        model.setSenderId("admin");
        model.setTitle("修改密码");
        model.setText("密码修改成功！");
        model.setUserIds(receiverIds);
        messageService.send(model);
    }

    public void update(UserUpdateVm model, HttpServletRequest request) throws Exception {
        Users entity = this.userDao.findById(model.getId()).orElseThrow(() -> new DoValidException("用户信息不存在"));
        BeanUtils.copyProperties(model, entity);

        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        this.userDao.save(entity);
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<Users> getSpecification(JSONObject paramObject, SpecificationBuilder<Users> builder) throws Exception {
        return builder.init(paramObject)
                .sContain("name", "name")
                .sContain("linkmanName", "linkmanName")
                .sContain("source", "source")
                .dOrder("createTime")
                .build();
    }

    private Users save(String phone) throws UnsupportedEncodingException {
        Users entity = new Users();

        entity.setId(BaseUtils.generate32Id());
        entity.setUsername(phone);
        entity.setPhone(phone);
        entity.setName(phone);
        entity.setPassword(AlgorithmHelper.md5_32("123456", false));
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById("admin");
        entity.setSource("公共服务平台");

        Date date = entity.getCreateTime().getTime();
        entity.setRegistDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        entity.setRegistTime(new SimpleDateFormat("HH:mm:ss").format(date));
        return this.userDao.save(entity);
    }

    private Optional<Users> findByNameOrPhone(String phoneOrUsername) {
        return userDao.findByUsernameOrPhone(phoneOrUsername, phoneOrUsername);
    }
}
