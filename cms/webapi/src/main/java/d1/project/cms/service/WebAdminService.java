package d1.project.cms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.SignInRetryLimitService;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.Constants;
import d1.project.cms.dao.WebAdminUserDao;
import d1.project.cms.dao.website.manage.RoleDao;
import d1.project.cms.dao.website.manage.UserRoleDao;
import d1.project.cms.entity.WebAdminUser;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.website.manage.Role;
import d1.project.cms.entity.website.manage.UserRole;
import d1.project.cms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.jws.WebService;
import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
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
    private final RoleDao roleDao;
    private final UserRoleDao userRoleDao;
    private final TokenService tokenService;
    private final SignInRetryLimitService signInRetryLimitService;

    public WebAdminService(WebAdminUserDao webAdminUserDao, RoleDao roleDao, UserRoleDao userRoleDao, TokenService tokenService, SignInRetryLimitService signInRetryLimitService) {
        this.webAdminUserDao = webAdminUserDao;
        this.roleDao = roleDao;
        this.userRoleDao = userRoleDao;
        this.tokenService = tokenService;
        this.signInRetryLimitService = signInRetryLimitService;
    }

    public SignInResult signIn(String userName, String password) throws Exception {
        if (StringUtils.isEmpty(userName) || StringUtils.isEmpty(password)) {
            throw new DoValidException("用户或密码不能为空");
        }
        if (signInRetryLimitService.verifyIsLocked(userName)) {
            throw new DoValidException("用户重试错误密码多次，导致用户被锁");
        }

        WebAdminUser user = webAdminUserDao.findByName(userName);
        if (user == null) {
            throw new DoValidException("用户不存在:" + userName);
        }
        if (!user.getPassword().equals(password)) {
            signInRetryLimitService.signInWithWrongPwd(userName);
            throw new DoValidException("密码不对");
        }
        signInRetryLimitService.signInSuccess(userName);

        user.setLastSignInTime(Calendar.getInstance());
        String authType = "webadmin";
        String token = tokenService.createToken(authType, (JSONObject) JSON.toJSON(user), Constants.TOKEN_EXPIRE_TIME);
        return new SignInResult(user.getId(), user.getName(), token);
    }

    public void updatePassword(String id, String oldPassword, String newPassword) throws Exception {
        if (StringUtils.isEmpty(id) || StringUtils.isEmpty(oldPassword) || StringUtils.isEmpty(newPassword)) {
            throw new DoValidException("用户id或新旧密码不能为空");
        }
        Optional<WebAdminUser> userOptional = webAdminUserDao.findById(id);
        if (!userOptional.isPresent()) {
            throw new DoValidException("用户不存在:" + id);
        }
        WebAdminUser user = userOptional.get();
        if (!user.getPassword().equals(oldPassword)) {
            throw new DoValidException("密码不对");
        }
        user.setPassword(newPassword);
        webAdminUserDao.save(user);
    }

    public void singOut(HttpServletRequest request) {
        tokenService.removeToken(request);
    }

    public String getUserId(HttpServletRequest request){
        JSONObject json = tokenService.getUserByToken(request);
        return json.getString("id");
    }

    public WebAdminUser getUser(HttpServletRequest request){
        JSONObject json = tokenService.getUserByToken(request);
        return json.toJavaObject(WebAdminUser.class);
    }

    public JSONObject findAll(JSONObject params) throws Exception {
        SpecificationBuilder<WebAdminUser> builder = new SpecificationBuilder<>();
        Specification<WebAdminUser> specification = builder.init(params)
                .sContain("name", "name")
                .sContain("tel", "tel")
                .dOrder("createTime").build();
        Page<WebAdminUser> data = this.webAdminUserDao.findAll(specification, builder.getPageable());

        JSONObject jsonObject = (JSONObject)JSON.toJSON(data);

        JSONArray content = jsonObject.getJSONArray("content");
        for(Object item : content) {
            String id = ((JSONObject) item).getString("id");
            UserRole userRole = this.userRoleDao.findFirstByUserId(id);
            if(userRole != null){
                Role role = this.roleDao.findById(userRole.getRoleId()).orElseThrow(()->new DoValidException("角色不存在"));
                ((JSONObject) item).put("roleName",role.getName());
            }

            String createTime = "";
            for(WebAdminUser user : data.getContent()){
                if(user.getId().equals(id) && user.getCreateTime() != null){
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    createTime = format.format(user.getCreateTime().getTime());
                    ((JSONObject) item).put("createTime",createTime);
                }
            }
        }

        return jsonObject;
    }

    public JSONObject findUserById(String id) throws DoValidException {
        WebAdminUser data = this.webAdminUserDao.findById(id).orElseThrow(()->new DoValidException("用户不存在"));
        UserRole userRole = this.userRoleDao.findFirstByUserId(data.getId());
        if(userRole == null) {
            throw new DoValidException("角色不存在");
        }
        Role role = this.roleDao.findById(userRole.getRoleId()).orElseThrow(()->new DoValidException("角色不存在"));
        JSONObject user = (JSONObject)JSON.toJSON(data);
        user.put("roleId",role.getId());
        user.put("roleName",role.getName());
        return user;
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void insert(HttpServletRequest request,JSONObject params){
        String userId = MyUtils.generate32Id();
        String roleId = params.getString("roleId");

        WebAdminUser user = JSON.toJavaObject(params,WebAdminUser.class);
        user.setId(userId);
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        this.tokenService.updateCreateIdAndTime(request,user);

        UserRole userRole = new UserRole();
        userRole.setId(MyUtils.generate32Id());
        userRole.setUserId(userId);
        userRole.setRoleId(roleId);

        this.webAdminUserDao.save(user);
        this.userRoleDao.save(userRole);
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void update(JSONObject params) throws DoValidException {
        String roleId = params.getString("roleId");

        WebAdminUser user = JSON.toJavaObject(params,WebAdminUser.class);
        WebAdminUser data = this.webAdminUserDao.findById(user.getId()).orElseThrow(() -> new DoValidException("用户不存在"));

        data.setName(user.getName());
        data.setRealName(user.getRealName());
        data.setPassword(user.getPassword());
        data.setSex(user.getSex());
        data.setEmail(user.getEmail());
        data.setTel(user.getTel());

        UserRole userRole = this.userRoleDao.findFirstByUserId(data.getId());
        userRole.setRoleId(roleId);

        this.webAdminUserDao.save(data);
        this.userRoleDao.save(userRole);
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void deleteById(String id) throws DoValidException {
        WebAdminUser user = this.webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("用户信息不存在"));
        this.webAdminUserDao.delete(user);
        UserRole userRole = this.userRoleDao.findFirstByUserId(user.getId());
        if(userRole != null) {
            this.userRoleDao.delete(userRole);
        }
    }

    public void resetPassword(String id) throws DoValidException {
        WebAdminUser user = this.webAdminUserDao.findById(id).orElseThrow(() -> new DoValidException("用户信息不存在"));
        user.setPassword("e10adc3949ba59abbe56e057f20f883e");
        this.webAdminUserDao.save(user);
    }
}
