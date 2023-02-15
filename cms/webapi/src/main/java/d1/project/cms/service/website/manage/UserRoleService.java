package d1.project.cms.service.website.manage;

import d1.project.cms.dao.website.manage.UserRoleDao;
import d1.project.cms.entity.website.manage.Role;
import d1.project.cms.entity.website.manage.UserRole;
import d1.project.cms.model.website.manage.UserRolePostVm;
import d1.project.cms.model.website.manage.UserRoleSelectVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Service
public class UserRoleService {

    @Autowired
    private UserRoleDao userRoleDao;

    @Autowired
    private RoleService roleService;

    public UserRoleSelectVm getRolesAndSelectByUserId(String userId) throws Exception {

        List<Role> roles = roleService.findAll();
        List<UserRole> userRoles = userRoleDao.findAllByUserId(userId);

        List<String> userRoles_roleIds = new ArrayList<>();
        userRoles.forEach(t -> userRoles_roleIds.add(t.getRoleId()));

        UserRoleSelectVm res = new UserRoleSelectVm();
        res.setRoles(roles);
        res.setChoose(userRoles_roleIds);

        return res;
    }


    public void insert(UserRolePostVm model) throws Exception {

        List<UserRole> userRoleOlds = userRoleDao.findAllByUserId(model.getUserId());
        userRoleDao.deleteAll(userRoleOlds);

        List<UserRole> userRoles = new ArrayList<>();

        List<String> roleIds = model.getRoleIds();
        if (roleIds != null) {
            roleIds.forEach(t -> {
                UserRole userRole = new UserRole();
                userRole.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
                userRole.setUserId(model.getUserId());
                userRole.setRoleId(t);
                userRoles.add(userRole);
            });
            userRoleDao.saveAll(userRoles);
        }
    }


    //用戶下所有角色的名称
    public List<String> findAllRoleNameByUserId(String userId) {
        return userRoleDao.findAllRoleNameByUserId(userId);
    }

    public List<UserRole> findAllByUserId(String userId) {
        return userRoleDao.findAllByUserId(userId);
    }

    public String findFirstIndex(String roleId){
        return userRoleDao.findFirstIndex(roleId);
    }

}
