package d1.project.cms.service.website.manage;

import d1.project.cms.dao.website.manage.RolePermissionDao;
import d1.project.cms.entity.website.manage.MenuTree;
import d1.project.cms.entity.website.manage.Role;
import d1.project.cms.entity.website.manage.RolePermission;
import d1.project.cms.model.website.manage.MenuTreeGetVm;
import d1.project.cms.model.website.manage.RolePermissionPostVm;
import d1.project.cms.model.website.manage.RolePermissionSelectVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionService {
    @Autowired
    private RolePermissionDao rolePermissionDao;

    @Autowired
    private RoleService roleService;

    @Autowired
    private MenuTreeService menuTreeService;

    public List<RolePermission> findRolePermissionByUserId(String userId) {
        return rolePermissionDao.findRolePermissionByUserId(userId);
    }


    public RolePermissionSelectVm getMenuAndSelectByRoleId(String roleId) throws Exception {

        //完整功能树
        List<MenuTreeGetVm> menuTrees = menuTreeService.findAllTree();

        //已选择功能树Id
        List<String> rolePermissionIds = new ArrayList<>();
        List<RolePermission> rolePermissionList = rolePermissionDao.findAllByRoleId(roleId);
        rolePermissionList.forEach(t -> {
            if (!menuTreeService.existsByParentId(t.getMenuTreeId())) {
                rolePermissionIds.add(t.getMenuTreeId());
            }
        });

        //拼装结果
        RolePermissionSelectVm rolePerAndMenuTree = new RolePermissionSelectVm();
        rolePerAndMenuTree.setMenuTree(menuTrees);
        rolePerAndMenuTree.setChoose(rolePermissionIds);

        return rolePerAndMenuTree;
    }


    public void insert(RolePermissionPostVm model, HttpServletRequest request) throws Exception {
        Role role = roleService.findById(model.getRoleId());
        //整体替换，删除原有数据
        rolePermissionDao.deleteAllByRoleId(model.getRoleId());

        //新添加权限列表
        List<MenuTree> menuTrees = menuTreeService.findAllById(model.getMenuTreeIds());

        List<RolePermission> rolePermissions = new ArrayList<>();

        for (int i = 0; i < menuTrees.size(); i++) {

            RolePermission rolePermission = new RolePermission();
            rolePermission.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
            rolePermission.setRoleId(model.getRoleId());
            rolePermission.setMenuTreeId(menuTrees.get(i).getId());

            rolePermissions.add(rolePermission);
        }
        rolePermissionDao.saveAll(rolePermissions);
    }

}
