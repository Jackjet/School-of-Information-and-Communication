package d1.project.questionnaire.controller.webadmin;

import d1.framework.webapi.DoBaseController;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.entity.user.WebAdminUser;
import d1.project.questionnaire.model.SignInResultVm;
import d1.project.questionnaire.model.UserMsg;
import d1.project.questionnaire.service.user.WebAdminUserService;
import d1.project.questionnaire.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Kikki 2019/8/26 9:47
 */
@Auth("webadmin")
@RestController("/webadmin/webAdminUser")
@RequestMapping("/webadmin/webAdminUser")
@Api(value = "/webadmin/webAdminUser", tags = "管理用户管理")
public class WebAdminUserController extends DoBaseController<WebAdminUser> {
    private final WebAdminUserService userService;

    public WebAdminUserController(WebAdminUserService userService) {
        this.userService = userService;
    }

    @Override
    protected DoServiceImpBase<WebAdminUser> getBaseService() {
        return userService;
    }

    /**
     * 登录
     *
     * @param username 用户名
     * @param password 密码
     * @return d1.framework.webapi.http.Result<d1.project.liangxin.model.SignInResultVm>
     * @author Kikki 2019/9/2 19:51
     **/
    @Auth(value = "noauth")
    @ApiOperation(value = "用户登录", notes = "根据用户名密码登录")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", required = true, dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", required = true, dataType = "String")
    })
    @RequestMapping(value = "/signIn", method = RequestMethod.GET)
    public Result<SignInResultVm> login(String username, String password) {
        try {
            SignInResultVm result = userService.signIn(username, password);
            return ResultUtil.success(Constants.SUCCESS, result);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }


    @ApiOperation("修改密码")
    @RequestMapping(value = {"/updatePwd"}, method = {RequestMethod.PUT})
    public Result<String> updatePwd(@RequestBody UserMsg model, HttpServletRequest request) {
        try {
            userService.updatePwd(model, request);
            return ResultUtil.success("修改密码成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail("修改失败：" + e.getMessage());
        }
    }

}
