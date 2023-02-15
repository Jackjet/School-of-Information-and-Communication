package d1.project.cms.controller.webadmin.website.manage;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.annotation.Permission;
import d1.project.cms.model.website.manage.MenuTreeGN;
import d1.project.cms.model.website.manage.MenuTreeGetVm;
import d1.project.cms.service.website.manage.MenuTreeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/MenuTree")
@Api(value = "/webadmin/permission/MenuTree", tags = "功能树管理")
public class MenuTreeController {

    @Autowired
    private MenuTreeService menuTreeService;

    @ApiOperation(value = "获取全部功能树")
    @GetMapping(value = "/findAll")
    public Result<List<MenuTreeGetVm>> findAllTree() {
        try {
            List<MenuTreeGetVm> mo = menuTreeService.findAllTree();
            return ResultUtil.success("获取全部功能树成功", mo);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "根据menuId获取功能树")
    @GetMapping(value = "/findByMenuId")
    public Result<MenuTreeGetVm> findByMenuId(String menuId) {
        try {
            MenuTreeGetVm mo = menuTreeService.findByMenuId(menuId);
            return ResultUtil.success("获取全部功能树成功", mo);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @GetMapping(value = "/findById")
    public Result<MenuTreeGN> findById(@Valid @NotBlank(message = "id不可为空") String id) {

        try {
            MenuTreeGN mUser = menuTreeService.findByIdGn(id);
            return ResultUtil.success("获取详细信息", mUser);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
    }

}
