package d1.project.caict.bigdata.goverance.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.model.UserRolePostVm;
import d1.framework.permission.service.UserRoleService;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.SignInRetryLimitService;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.dao.WebAdminUserDao;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.DigestUtils;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

/**
 * 针对webadmin的登录等操作
 *
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class WebAdminService {
    private final WebAdminUserDao webAdminUserDao;

    private final TokenService tokenService;
    private final OperationLogService operationLogService;
    private final SignInRetryLimitService signInRetryLimitService;
    private final UserRoleService userRoleService;
    private String authType = "webadmin";

    public WebAdminService(WebAdminUserDao webAdminUserDao, TokenService tokenService, OperationLogService operationLogService, SignInRetryLimitService signInRetryLimitService, UserRoleService userRoleService) {
        this.webAdminUserDao = webAdminUserDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
        this.signInRetryLimitService = signInRetryLimitService;
        this.userRoleService = userRoleService;
    }


    /**
     * 新增
     *
     * @param jsonObject 参数
     * @param request    客户端请求
     * @author Kikki  2020/7/3 20:56
     */
    public void insert(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = getCurrentWebadminUserEntity(request);
        String userId = currentWebadminUserEntity.getId();
        String name = jsonObject.getString("name");
        WebAdminUser userRelative1 = JSON.parseObject(jsonObject.toJSONString(), WebAdminUser.class);
        try {
            insertUser(userRelative1, userId);
            operationLogService.insert(new OperationLog("用户管理—添加", "运维行为管理—用户管理", "添加了一个用户:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户管理—添加", "运维行为管理—用户管理", "添加了一个用户:未知", "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            /*这个异常不进log.txt日志里*/
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 不对外暴露的新增
     *
     * @param webAdminUser 用户
     * @param userId       创建人id
     * @author Kikki  2020/7/3 20:53
     */
    @Transactional(rollbackFor = Exception.class)
    void insertUser(WebAdminUser webAdminUser, String userId) throws Exception {
        WebAdminUser adminUser1 = MyUtils.initInsert(webAdminUser, userId);
        MyUtils.throwMsg(webAdminUserDao.existsByName(adminUser1.getName()), "用户名称已存在");
        MyUtils.throwMsg(webAdminUserDao.existsByTel(adminUser1.getTel()), "手机号已存在");
        adminUser1.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
        webAdminUserDao.save(adminUser1);

        UserRolePostVm userRolePostVm = new UserRolePostVm();
        List<String> roleIds = Collections.singletonList(adminUser1.getRoleId());
        userRolePostVm.setUserId(adminUser1.getId());
        userRolePostVm.setRoleIds(roleIds);
        userRoleService.insert(userRolePostVm);
    }

    /**
     * 删除
     *
     * @param id      用户id
     * @param request 客户端请求
     * @author Kikki  2020/7/3 20:59
     */
    public void delete(String id, HttpServletRequest request) throws Exception {
        WebAdminUser webAdminUser = webAdminUserDao.findById(id).orElseThrow(() -> new Exception("该用户不存在"));
        WebAdminUser currentWebadminUserEntity = getCurrentWebadminUserEntity(request);
        String name = webAdminUser.getName();
        try {
            deleteUser(id);
            operationLogService.insert(new OperationLog("用户管理—删除", "运维行为管理—用户管理", "删除了一个用户:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户管理—删除", "运维行为管理—用户管理", "删除了一个用户:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            /*这个异常不进log.txt日志里*/
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 不对外暴露的删除
     *
     * @param id 用户id
     * @author Kikki  2020/7/3 20:59
     */
    @Transactional(rollbackFor = Exception.class)
    void deleteUser(String id) throws Exception {
        MyUtils.throwMsg(StringUtils.isEmpty(id), "id不能为空");
        MyUtils.throwMsg("admin".equals(id), "admin用户不可删除");
        webAdminUserDao.deleteById(id);
        UserRolePostVm userRolePostVm = new UserRolePostVm();
        userRolePostVm.setUserId(id);
        userRoleService.insert(userRolePostVm);

    }

    /**
     * 修改
     *
     * @param request 客户端请求
     * @param model   参数
     * @author Kikki  2020/7/3 21:31
     */
    public void update(HttpServletRequest request, JSONObject model) throws Exception {
        WebAdminUser oldWebAdminUser = webAdminUserDao.findById(model.getString("id")).orElseThrow(() -> new Exception("该用户不存在"));
        WebAdminUser currentWebadminUserEntity = getCurrentWebadminUserEntity(request);
        String name = oldWebAdminUser.getName();
        try {
            updateUser(request, model);
            operationLogService.insert(new OperationLog("用户管理—编辑", "运维行为管理—用户管理", "编辑了一个用户:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户管理—编辑", "运维行为管理—用户管理", "编辑了一个用户:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            /*这个异常不进log.txt日志里*/
            throw new ValidException(e.getMessage());
        }

    }

    /**
     * 不对外暴露的修改
     *
     * @param request 客户端请求
     * @param model   参数
     * @author Kikki  2020/7/3 21:33
     */
    @Transactional(rollbackFor = Exception.class)
    void updateUser(HttpServletRequest request, JSONObject model) throws Exception {
        String currentUserId = tokenService.getUserByToken(request).getString("id");
        WebAdminUser webAdminUser = MyUtils.model2Entity(model, WebAdminUser.class);
        WebAdminUser oldWebAdminUser = webAdminUserDao.findById(webAdminUser.getId()).orElseThrow(() -> new Exception("该用户不存在"));
        MyUtils.throwMsg(webAdminUserDao.existsByNameAndIdNot(webAdminUser.getName(), webAdminUser.getId()), "用户名称已存在");
        MyUtils.throwMsg(webAdminUserDao.existsByTelAndIdNot(webAdminUser.getTel(), webAdminUser.getId()), "手机号已存在");
        /* 保存信息 */
        WebAdminUser app1 = MyUtils.initUpdate(webAdminUser, currentUserId);
        WebAdminUser app2 = MyUtils.copyProperties(app1, oldWebAdminUser);
        webAdminUserDao.save(app2);

        UserRolePostVm userRolePostVm = new UserRolePostVm();
        List<String> roleIds = Collections.singletonList(oldWebAdminUser.getRoleId());
        userRolePostVm.setUserId(oldWebAdminUser.getId());
        userRolePostVm.setRoleIds(roleIds);
        userRoleService.insert(userRolePostVm);
    }

    public Page<WebAdminUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<WebAdminUser> builder = new SpecificationBuilder<>();
        Specification<WebAdminUser> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tel", "tel")
                .sContain("organization", "organization")
                .sEqual("roleId", "roleId")
                .dOrder("createTime")
                .build();
        return webAdminUserDao.findAll(specification, builder.getPageable());
    }

    public Optional<WebAdminUser> findById(String id) {
        return webAdminUserDao.findById(id);
    }

    /**
     * 登录
     *
     * @param userName
     * @param password
     * @param request
     * @return d1.framework.webapi.model.SignInResult
     * @author Kikki  2020/7/3 21:33
     */
    public SignInResult signIn(String userName, String password, HttpServletRequest request) throws Exception {
        if (StringUtils.isEmpty(userName)) {
            throw new ValidException("用户不能为空");
        }
        if (signInRetryLimitService.verifyIsLocked(userName)) {
            throw new ValidException("用户重试错误密码多次，导致用户被锁");
        }

        WebAdminUser user = webAdminUserDao.findByName(userName);
        if (user == null) {
            throw new ValidException("用户不存在:" + userName);
        }

        if (!user.getPassword().equals(password)) {
            signInRetryLimitService.signInWithWrongPwd(userName);
            throw new ValidException("密码不对");
        }
        signInRetryLimitService.signInSuccess(userName);

        user.setLastSignInTime(Calendar.getInstance());
        webAdminUserDao.save(user);
        String token = tokenService.createToken(authType, (JSONObject) JSON.toJSON(user), Constants.TOKEN_EXPIRE_TIME);
        return new SignInResult(user.getId(), user.getName(), token);
    }


    /**
     * 清除token
     *
     * @param request
     * @return void
     * @author Kikki  2020/7/3 21:35
     */
    public void singOut(HttpServletRequest request) {
        tokenService.removeToken(request);
    }

    public void verifyUserRole(HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = getCurrentWebadminUserEntity(request);
        if (StringUtils.isEmpty(currentWebadminUserEntity.getRoleId())) {
            throw new ValidException("您没有该平台功能相关权限，请联系系统管理员！");
        }
    }

    /**
     * 修改密码
     *
     * @param request     客户端请求
     * @param id          id
     * @param oldPassword 旧密码
     * @param newPassword 新密码
     * @return void
     * @author Kikki  2020/7/3 21:34
     */
    public void updatePassword(HttpServletRequest request, String id, String oldPassword, String newPassword) throws Exception {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(newPassword)) {
            throw new ValidException("用户id或新旧密码不能为空");
        }
        Optional<WebAdminUser> userOptional = webAdminUserDao.findById(id);
        if (!userOptional.isPresent()) {
            throw new ValidException("用户不存在:" + id);
        }
        WebAdminUser user = userOptional.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw new ValidException("旧密码错误");
        }
        user.setPassword(newPassword);
        webAdminUserDao.save(user);
    }

    /**
     * 重置密码
     *
     * @param request 客户端请求
     * @param id      id
     * @author Kikki  2020/7/3 21:34
     */
    public void resetPassword(HttpServletRequest request, String id) throws Exception {
        WebAdminUser oldWebAdminUser = webAdminUserDao.findById(id).orElseThrow(() -> new Exception("该用户不存在"));
        WebAdminUser currentWebadminUserEntity = getCurrentWebadminUserEntity(request);
        String name = oldWebAdminUser.getName();
        try {
            if ("admin".equals(oldWebAdminUser.getId())) {
                throw new ValidException("admin用户不可重置密码");
            }
            tokenService.updateUpdateIdAndTime(request, oldWebAdminUser);
            oldWebAdminUser.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes(StandardCharsets.UTF_8)));
            webAdminUserDao.save(oldWebAdminUser);
            operationLogService.insert(new OperationLog("用户管理—重置密码", "运维行为管理—用户管理", "重置了用户:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户管理—重置密码", "运维行为管理—用户管理", "重置了用户:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 获取当前应用程序用户实体
     *
     * @param request 请求
     * @return d1.project.nankai.entity.user.AppUser
     * @author Kikki  2020/3/29 0:21
     */
    public WebAdminUser getCurrentWebadminUserEntity(HttpServletRequest request) throws Exception {
        tokenService.getUserByToken(request).getString("id");
        return findById(tokenService.getUserByToken(request).getString("id")).orElseThrow(() -> new Exception("当前用户不存在"));
    }

    public boolean existsByGroupWebAdminUseName(String groupWebAdminUseName) {
        return webAdminUserDao.existsByGroupWebAdminUseName(groupWebAdminUseName);
    }
}
