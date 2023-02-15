package d1.project.questionnaire.service.user;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.service.impl.DoUserServiceImplBase;
import d1.project.questionnaire.dao.user.WebAdminUserDao;
import d1.project.questionnaire.entity.user.WebAdminUser;
import d1.project.questionnaire.model.SignInResultVm;
import d1.project.questionnaire.model.UserMsg;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.util.ThrowsException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * @author Kikki 2019/8/26 10:15
 */
@Service
public class WebAdminUserService extends DoUserServiceImplBase<WebAdminUser> {
    @Autowired
    ThrowsException throwsException;
    @Autowired
    private WebAdminUserDao userDao;

    @Override
    protected JpaRepository<WebAdminUser, String> getDao() {
        return userDao;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return d1.project.liangxin.model.SignInResultVm
     * @author Kikki 2019/9/2 20:26
     **/
    public SignInResultVm signIn(String username, String password) throws Exception {
        SignInResultVm result = new SignInResultVm();
        WebAdminUser user = userDao.findByName(username);
        if (user == null) {
            throw new Exception("用户：" + username + "不存在");
        }
        if (!user.getPassword().equals(password)) {
            throw new Exception(Constants.PASSWORD_ERROR);
        }
        signInWithoutSingle(user, 30 * 24 * 60 * 60);
        result.setId(user.getId());
        result.setToken(user.getAccessToken());
        result.setName(user.getName());
        return result;
    }

    /**
     * 插入前
     *
     * @param obj 用户
     * @author Kikki 2019/9/2 20:27
     **/
    @Override
    protected void beforeInsert(WebAdminUser obj) throws Exception {
        super.beforeInsert(obj);
        throwsException.webAdminBeforeInsert(obj.getName(), obj.getPassword(), userDao.existsByName(obj.getName()));
    }

    /**
     * 更新前
     *
     * @param obj 用户
     * @author Kikki 2019/9/2 20:27
     **/
    @Override
    protected void beforeUpdate(WebAdminUser obj) throws Exception {
        super.beforeUpdate(obj);
        int i = userDao.findOtherUserByNameOrPhone(obj.getPhone(), obj.getName(), obj.getId());
        throwsException.webAdminBeforeUpdate(i);
    }

    /**
     * 获取当前用户实体
     *
     * @param request 请求
     * @return WebAdminUser
     * @author Kikki 2019/9/2 20:25
     **/
    public WebAdminUser getCurrentUserEntity(HttpServletRequest request) throws Exception {
        JSONObject data = getCurrentUser(request);
        String userId = data.getString("id");
        WebAdminUser user = findById(userId);
        if (user == null) {
            throw new Exception("在未能找到当前用户!");
        }
        return user;
    }

    /**
     * 获取当前用户ID
     *
     * @param request 请求
     * @return java.lang.String
     * @author Kikki 2019/9/2 20:24
     **/
    public String getCurrentUserId(HttpServletRequest request) throws Exception {
        JSONObject data = getCurrentUser(request);
        if (data == null) {
            throw new Exception(Constants.THIS_USER_HAS_NO_PRIVILEGES);
        }
        return data.getString("id");
    }


    /**
     * 管理员修改密码
     *
     * @param model 修改密码
     * @throws Exception 异常处理
     */
    public void updatePwd(UserMsg model, HttpServletRequest request) throws Exception {
        String userId = getCurrentUserId(request);
        String id = model.getId();
        throwsException.userCheck(id, userId);
        Optional<WebAdminUser> user = userDao.findById(id);
        if (!user.isPresent()) {
            throw new Exception(Constants.USER_IS_NULL);
        }
        WebAdminUser webAdminUser = user.get();
        webAdminUser.setPassword(model.getNewPwd());
        userDao.save(webAdminUser);
    }
}
