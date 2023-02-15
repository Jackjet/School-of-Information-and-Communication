package d1.project.caict.system.controller.webadmin;

import com.alibaba.excel.EasyExcel;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.SignInResult;
import d1.framework.webapi.utils.CacheManger;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.entity.WebAdminUser;
import d1.project.caict.system.model.WebAdminUserExcelExport;
import d1.project.caict.system.model.WebAdminUserExcelImport;
import d1.project.caict.system.model.WebAdminUserVm;
import d1.project.caict.system.model.vm.*;
import d1.project.caict.system.service.WebAdminUserService;
import d1.project.caict.system.service.WebAdminUserUploadDataListener;
import net.dcrun.component.ehcache.IEhcacheService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

/**
 * d1project
 * 系统管理_用户管理
 *
 * @author kikki
 * @date 2020-09-07 15:25
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/webAdminUser")
public class WebAdminUserController {

    private final WebAdminUserService webAdminUserService;

    public WebAdminUserController(WebAdminUserService webAdminUserService) {
        this.webAdminUserService = webAdminUserService;
    }

    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody WebAdminUserInsertVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.insert(model, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/deleteAll")
    public Result<String> delete(@Valid @Size(min = 1, message = "无效操作") @RequestParam("ids") List<String> ids, HttpServletRequest request) throws DoValidException {
        webAdminUserService.deleteAll(ids, request);
        return ResultUtil.success("");
    }

    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "无效操作") String id, HttpServletRequest request) throws DoValidException {
        webAdminUserService.delete(id, request);
        return ResultUtil.success("");
    }

    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody WebAdminUserUpdateVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.update(model, request);
        return ResultUtil.success("");
    }

    /**
     * 修改密码
     */
    @PutMapping(value = {"/updatePassword"})
    public Result<String> updatePassword(@Valid @RequestBody WebAdminUserUpdatePasswordVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updatePassword(model, request);
        return ResultUtil.success("");
    }

    /**
     * 重置密码列表
     */
    @PutMapping(value = {"/updatePasswordReset"})
    public Result<String> updatePasswordReset(@Valid @RequestBody WebAdminUserUpdatePasswordResetVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updatePasswordReset(model, request);
        return ResultUtil.success("");
    }

    /**
     * 启用禁用列表
     */
    @PutMapping(value = {"/updateAllEnable"})
    public Result<String> updateAllEnable(@Valid @RequestBody WebAdminUserUpdateAllEnableVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updateAllEnable(model, request);
        return ResultUtil.success("");
    }

    /**
     * 启用禁用
     */
    @PutMapping(value = {"/updateEnable"})
    public Result<String> updateAllEnable(@Valid @RequestBody WebAdminUserUpdateEnableVm model, HttpServletRequest request) throws DoValidException {
        webAdminUserService.updateEnable(model, request);
        return ResultUtil.success("");
    }

    /**
     * 查询所有
     */
    @GetMapping(value = "/findAll")
    public Result<Page<WebAdminUser>> findAll(WebAdminUserFindAllVm model) throws Exception {
        return ResultUtil.success("", webAdminUserService.findAll(model));
    }

    /**
     * 详情
     */
    @GetMapping(value = "/find")
    public Result<WebAdminUserVm> find(@RequestParam String id) throws DoValidException {
        return ResultUtil.success("", webAdminUserService.find(id));
    }

    /**
     * 登录
     */
    @Auth("noauth")
    @GetMapping(value = {"/login"})
    public Result<SignInResult> login(@Valid @NotBlank(message = "用户名不可为空") String username, @Valid @NotBlank(message = "密码不可为空") String password, String x_index, String chenckMoveid, HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", webAdminUserService.login(username, password, x_index, chenckMoveid, request));
    }

    /**
     * 启用禁用
     */
    @GetMapping(value = "/signOut")
    public Result<String> signOut(HttpServletRequest request) throws DoValidException {
        webAdminUserService.singOut(request);
        return ResultUtil.success("");
    }

    /**
     * 导出
     */
    @PostMapping("/export")
    public Result<String> export(WebAdminUserFindAllVm model, HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<WebAdminUserExcelExport> allList = webAdminUserService.findAllList(model);
        EasyExcel.write(response.getOutputStream(), WebAdminUserExcelExport.class).sheet("用户").doWrite(allList);
        return null;
    }

    /**
     * 导出模板
     */
    @PostMapping("/exportModel")
    public Result<String> exportModel(HttpServletResponse response) throws Exception {
        // 这里注意 有同学反应使用swagger 会导致各种问题，请直接用浏览器或者用postman
        response.setContentType("application/vnd.ms-excel");
        response.setCharacterEncoding("utf-8");
        // 这里URLEncoder.encode可以防止中文乱码 当然和easyexcel没有关系
        String fileName = URLEncoder.encode("用户", "UTF-8").replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + fileName + ".xlsx");
        List<WebAdminUserExcelExport> data = new ArrayList<>();
        EasyExcel.write(response.getOutputStream(), WebAdminUserExcelImport.class).sheet("用户").doWrite(data);
        return null;
    }

    /**
     * 导入
     */
    @PostMapping("/import")
    public Result<String> upload(MultipartFile file, HttpServletRequest request) throws IOException, DoValidException {
        String uuId = "webAdminUserImport/" + BaseUtils.generate32Id();
        IEhcacheService cache = CacheManger.getInstance().getCache();
        EasyExcel.read(file.getInputStream(), WebAdminUserExcelImport.class, new WebAdminUserUploadDataListener(cache, webAdminUserService, request, uuId)).sheet().doReadSync();
        if (cache.containsKey(uuId)) {
            String string = cache.getString(uuId);
            cache.removeData(uuId);
            throw new DoValidException(string);
        }
        return ResultUtil.success("");
    }

}
