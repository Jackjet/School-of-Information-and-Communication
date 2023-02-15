package d1.project.caict.system.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.system.entity.RoleUser;
import d1.project.caict.system.model.vm.RoleUserFindRoleIdVm;
import d1.project.caict.system.model.vm.RoleUserMenuTreeVm;
import d1.project.caict.system.service.RoleUserService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * d1project
 * 系统管理_角色权限
 *
 * @author kikki
 * @date 2020-09-09 09:35
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/system/roleUser")
public class RoleUserController {

    private final RoleUserService roleUserService;

    public RoleUserController(RoleUserService roleUserService) {
        this.roleUserService = roleUserService;
    }

    @GetMapping(value = "/findRoleId")
    public Result<Page<RoleUser>> findRoleId(@Valid RoleUserFindRoleIdVm model) throws Exception {
        return ResultUtil.success("", roleUserService.findRoleId(model));
    }


    @GetMapping(value = "/findCurrentUser")
    public Result<List<RoleUserMenuTreeVm>> findCurrentUser(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUser(request));
    }

    @GetMapping(value = "/findCurrentUserButton")
    public Result<List<RoleUserMenuTreeVm>> findCurrentUserButton(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUserButton(request));
    }

    @GetMapping(value = "/findCurrentUserSidebar")
    public Result<List<RoleUserMenuTreeVm>> findCurrentUserSidebar(HttpServletRequest request) throws DoValidException {
        return ResultUtil.success("", roleUserService.findCurrentUserSidebar(request));
    }
}
