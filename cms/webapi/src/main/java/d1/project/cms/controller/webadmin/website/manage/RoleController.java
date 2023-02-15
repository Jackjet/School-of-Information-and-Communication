package d1.project.cms.controller.webadmin.website.manage;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.annotation.Permission;
import d1.project.cms.entity.website.manage.Role;
import d1.project.cms.model.website.manage.RoleGetVm;
import d1.project.cms.model.website.manage.RolePostVm;
import d1.project.cms.model.website.manage.RolePutVm;
import d1.project.cms.service.WebAdminService;
import d1.project.cms.service.website.manage.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/role")
@Api(value = "/webadmin/permission/role", tags = "角色管理")
public class RoleController {

    @Autowired
    private RoleService roleService;
    @Autowired
    private WebAdminService webAdminService;

    @ApiOperation(value = "获取所有角色")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "page", value = "第几页,从1开始", dataType = "int"),
            @ApiImplicitParam(name = "size", value = "每页几个，从1开始", dataType = "int")
    })
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Role>> findAllWithPage(RoleGetVm model) {
        try {
            Page<Role> result = roleService.findAll(model);
            return ResultUtil.success("获取所有角色成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }

    }

    @ApiOperation(value = "获取所有角色下拉列表")
    @RequestMapping(value = "/findAllList", method = RequestMethod.GET)
    public Result<List<Role>> findAllList() {
        try {
            List<Role> result = roleService.findAllRoles();
            Role admin = null;
            for (Role role : result) {
                if ("admin".equals(role.getId())) {
                    admin = role;
                }
            }
            if (admin != null) {
                result.remove(admin);
            }

            return ResultUtil.success("获取所有角色成功", result);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }

    }

    @ApiOperation(value = "创建角色")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result insert(@RequestBody RolePostVm model, HttpServletRequest request) {
        try {
            roleService.insert(model, request);

            return ResultUtil.success("新增成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新角色")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result update(@RequestBody RolePutVm model, HttpServletRequest request) {
        try {
            roleService.update(model, request);
            return ResultUtil.success("编辑成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除角色")
    @RequestMapping(value = "/deleteRole", method = RequestMethod.DELETE)
    public Result deleteRole(String id, HttpServletRequest request) {
        try {
            roleService.deleteUser(id, request);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败" + e.getMessage());
        }
    }

    @ApiOperation(value = "获取数据详细信息", notes = "根据id来获取详细信息")
    @ApiImplicitParam(name = "id", value = "数据ID", required = true, dataType = "String")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Role> findById(String id) {
        Role role = null;
        try {
            role = roleService.findById(id);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage());
        }
        return role != null ? ResultUtil.success("获取详细信息", role) : ResultUtil.fail("数据为空或获取失败!");
    }

}
