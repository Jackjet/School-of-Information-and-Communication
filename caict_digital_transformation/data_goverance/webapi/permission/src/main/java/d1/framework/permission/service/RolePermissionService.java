package d1.framework.permission.service;

import d1.framework.permission.dao.RolePermissionDao;
import d1.framework.permission.entity.MenuTree;
import d1.framework.permission.entity.RolePermission;
import d1.framework.permission.model.MenuTreeGetVm;
import d1.framework.permission.model.RolePermissionPostVm;
import d1.framework.permission.model.RolePermissionSelectVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class RolePermissionService {

    @Autowired
    private RolePermissionDao rolePermissionDao;

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


    public void insert(RolePermissionPostVm model) {

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
