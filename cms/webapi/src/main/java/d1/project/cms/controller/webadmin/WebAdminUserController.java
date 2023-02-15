package d1.project.cms.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.model.SignInResult;
import d1.project.cms.entity.WebAdminUser;
import d1.project.cms.service.WebAdminService;
import io.swagger.annotations.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * @author Buter
 * @date 2020/3/18 17:12
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/user")
@Api(value = "/webadmin/user", tags = "管理账号管理")
public class WebAdminUserController {
    private final WebAdminService userService;

    @Autowired
    public WebAdminUserController(WebAdminService userService) {
        this.userService = userService;
    }

    @Auth("noauth")
    @ApiOperation(value = "用户登录", notes = "根据用户名密码登录")
    @ApiImplicitParams({@ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", defaultValue = "e10adc3949ba59abbe56e057f20f883e", required = true, dataType = "String")})
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public Result<SignInResult> signIn(String username, String password) {
        try {
            SignInResult result = userService.signIn(username, password);
            return ResultUtil.success("登录成功", result);
        } catch (Exception e) {
            return ResultUtil.fail("登录失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "修改密码", notes = "修改密码")
    @RequestMapping(value = "/updatePassword", method = RequestMethod.PUT)
    public Result<String> updatePassword(@RequestBody UpdatePwdVm model) {
        try {
            this.userService.updatePassword(model.getId(), model.getOldPassword(), model.getNewPassword());
            return ResultUtil.success("修改成功");
        } catch (Exception e) {
            return ResultUtil.fail("修改失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "注销登录", notes = "注销登录")
    @RequestMapping(value = "/signOut", method = RequestMethod.GET)
    public Result<String> signOut(HttpServletRequest request) {
        try {
            this.userService.singOut(request);
            return ResultUtil.success("注销成功");
        } catch (Exception e) {
            return ResultUtil.fail("注销失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<String> findById(HttpServletRequest request) {
        try {
            WebAdminUser user = this.userService.getUser(request);
            return ResultUtil.success("查询成功", user);
        } catch (Exception e) {
            return ResultUtil.fail("注销失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "用户列表", notes = "用户列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<JSONObject> findAll(WebAdminUserFindAllGetVm params) throws Exception {
        JSONObject result = this.userService.findAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "查询用户信息", notes = "查询用户信息")
    @RequestMapping(value = "/findUserById", method = RequestMethod.GET)
    public Result<JSONObject> findUserById(String id) throws DoValidException {
        JSONObject user = this.userService.findUserById(id);
        return ResultUtil.success("查询成功", user);
    }

    @ApiOperation(value = "添加用户信息", notes = "添加用户信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<JSONObject> insert(HttpServletRequest request, @RequestBody WebAdminUserInsertPostVm params) {
        this.userService.insert(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("添加成功");
    }

    @ApiOperation(value = "修改用户信息", notes = "修改用户信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<JSONObject> update(@RequestBody WebAdminUserUpdatePutVm params) throws DoValidException {
        this.userService.update((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("修改成功");
    }

    @ApiOperation(value = "删除用户信息", notes = "删除用户信息")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<JSONObject> deleteById(String id) throws DoValidException {
        this.userService.deleteById(id);
        return ResultUtil.success("删除成功");
    }

    @ApiOperation(value = "重置密码", notes = "重置密码")
    @RequestMapping(value = "/resetPassword", method = RequestMethod.PUT)
    public Result<JSONObject> resetPassword(@RequestBody WebAdminUserResetPasswordPutVm params) throws DoValidException {
        this.userService.resetPassword(params.getId());
        return ResultUtil.success("重置成功");
    }
}

class UpdatePwdVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty(value = "id")
    private String id;
    @NotBlank(message = "旧的密码不能为空")
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;
    @NotBlank(message = "新的密码不能为空")
    @ApiModelProperty(value = "新密码")
    private String newPassword;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOldPassword() {
        return oldPassword;
    }

    public void setOldPassword(String oldPassword) {
        this.oldPassword = oldPassword;
    }

    public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }
}

class WebAdminUserFindAllGetVm extends PageableVm {
    private String name;
    private String tel;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }
}

class WebAdminUserInsertPostVm {
    @NotBlank(message = "账号不能为空")
    private String name;
    @NotBlank(message = "用户名称不能为空")
    private String realName;
    private String sex;
    private String email;
    private String tel;
    private String roleId;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

class WebAdminUserUpdatePutVm {
    @NotBlank(message = "用户id不能为空")
    private String id;
    @NotBlank(message = "账号不能为空")
    private String name;
    @NotBlank(message = "用户名称不能为空")
    private String realName;
    private String sex;
    private String email;
    private String tel;
    private String roleId;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }
}

class WebAdminUserResetPasswordPutVm {
    @NotBlank(message = "用户id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
