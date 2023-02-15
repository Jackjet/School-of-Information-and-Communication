package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.SignInResult;
import d1.project.caict.business.entity.Users;
import d1.project.caict.business.model.*;
import d1.project.caict.business.service.UserService;
import d1.project.caict.business.utils.Constants;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.io.UnsupportedEncodingException;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/users")
public class UserController {
    private final UserService userService;
    private final IFileServerService fileServerService;

    public UserController(UserService userService, IFileServerService fileServerService) {
        this.userService = userService;
        this.fileServerService = fileServerService;
    }

    @Auth("webadmin")
    @GetMapping(value = "/findAll")
    public Result<Page<Users>> findAll(@Valid UserFindAllVm model) throws Exception {
        return ResultUtil.success("", userService.findAll(model));
    }

    @Auth({"webadmin", "web"})
    @GetMapping(value = "/find")
    public Result<Users> find(@Valid @NotBlank(message = "id不能为空") String id) throws Exception {
        return ResultUtil.success("", userService.find(id));
    }

    @Auth("webadmin")
    @PutMapping(value = "/resetPwd")
    public Result resetPwd(@Valid @RequestBody UserResetPwdUpdateVm model, HttpServletRequest request) throws DoValidException {
        userService.resetPwd(model.getId(), model.getPassword(), model.getPlaintext(), request);
        return ResultUtil.success();
    }

    @Auth("webadmin")
    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        userService.delete(id);
        return ResultUtil.success();
    }

    /**
     * 发送手机验证码
     */
    @Auth("noauth")
    @PostMapping(value = "/code")
    public Result sendVerificationCode(@Valid @RequestBody UserSendCodeVm model) throws Exception {
        userService.sendVerificationCode(model.getType(), model.getPhone());
        return ResultUtil.success("验证码已发送");
    }

    /**
     * 手机号验证码登录注册
     */
    @Auth("noauth")
    @PostMapping(value = "/login/phone")
    public Result<SignInResult> login(@Valid @RequestBody UserPhoneLoginVm model) throws DoValidException, UnsupportedEncodingException {
        return ResultUtil.success("", userService.loginWithPhone(model.getPhone(), model.getCode()));
    }

    /**
     * 手机号/用户名 密码登录
     */
    @Auth("noauth")
    @PostMapping(value = "/login/password")
    public Result<SignInResult> login(@Valid @RequestBody UserPasswordLoginVm model) throws DoValidException {
        return ResultUtil.success("", userService.loginWithPassword(model.getPhoneOrUsername(), model.getPassword()));
    }


    @PutMapping(value = "/update/password")
    public Result insert(@Valid @RequestBody UserUpdatePasswordVm model, HttpServletRequest request) throws Exception {
        userService.updatePassword(model.getPhone(), model.getCode(), model.getPassword(), request);
        return ResultUtil.success();
    }

    @PutMapping(value = "/update")
    public Result insert(@Valid @RequestBody UserUpdateVm model, HttpServletRequest request) throws Exception {
        userService.update(model, request);
        return ResultUtil.success();
    }

    /**
     * 注销
     */
    @GetMapping(value = "/signOut")
    public Result signOut(HttpServletRequest request) throws DoValidException {
        userService.singOut(request);
        return ResultUtil.success();
    }


    /**
     * 上传公司logo
     *
     * @param file 文件
     * @return 文件上传信息
     * @throws Exception 异常
     */
    @PostMapping(value = "/upload/users/logo")
    public UploadResult uploadLogo(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServerService.uploadFile(file, Constants.USERS, "web/users/download/users/logo");
    }

    /**
     * 下载公司logo
     *
     * @param fileName 文件名称
     * @param request  request
     * @return 下载流
     * @throws Exception 异常
     */
    @Auth("noauth")
    @GetMapping(value = "/download/users/logo/{fileName:.+}")
    public ResponseEntity<Resource> downloadLogo(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.USERS, fileName);
    }
}
