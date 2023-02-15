package d1.project.caict.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.system.service.BaseService;
import net.dcrun.component.osinfo.OsInfoService;
import net.dcrun.component.osinfo.model.OsInfo;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * d1Project
 * 系统管理_系统设置_数据备份
 *
 * @author kikki
 * @date 2020-09-12 23:56
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/base")
public class BaseController {

    private final BaseService baseService;

    public BaseController(BaseService baseService) {
        this.baseService = baseService;
    }


    /**
     * 获取系统信息
     */
    @GetMapping(value = "/findOsInfo")
    public Result<OsInfo> findOsInfo() throws Exception {
        OsInfoService osInfoService = new OsInfoService();
        return ResultUtil.success("", osInfoService.getOsInfo());
    }

    /**
     * 获取数据库信息
     */
    @GetMapping(value = "/findDatabaseInfo")
    public Result<String> findDatabaseInfo() {
        return ResultUtil.success("", baseService.findDatabaseInfo());
    }


    /**
     * 获取验证滑块
     */
    @Auth("noauth")
    @RequestMapping(value = "/getImageVerifyCode", method = RequestMethod.POST)
    public Result<String> getImageVerifyCode() throws Exception {
        return ResultUtil.success("", baseService.getImageVerifyCode());
    }

}
