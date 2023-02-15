package d1.project.cms.service.website.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.dao.website.manage.RoleDao;
import d1.project.cms.dao.website.manage.RolePermissionDao;
import d1.project.cms.dao.website.manage.UserRoleDao;
import d1.project.cms.entity.website.manage.Role;
import d1.project.cms.entity.website.manage.RolePermission;
import d1.project.cms.entity.website.manage.UserRole;
import d1.project.cms.model.website.manage.RoleGetVm;
import d1.project.cms.model.website.manage.RolePostVm;
import d1.project.cms.model.website.manage.RolePutVm;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class RoleService {
    @Autowired
    private RoleDao roleDao;
    @Autowired
    private RolePermissionDao rolePermissionDao;
    @Autowired
    private UserRoleDao userRoleDao;


    public void insert(RolePostVm model, HttpServletRequest request) throws Exception {
        String name = model.getName();
        if (name == null) {
            throw new DoValidException("角色名称不可为空");
        }
        if (roleDao.findByName(name) != null) {
            throw new DoValidException("角色名称已存在");
        }
        Role role = new Role();
        role.setId(UUID.randomUUID().toString().replace("-", "").toLowerCase());
        role.setName(model.getName());
        role.setRemark(model.getRemark());
        role.setCreateTime(new Date());

        roleDao.save(role);
    }


    public void update(RolePutVm model, HttpServletRequest request) throws Exception {
        if (model == null) {
            throw new DoValidException("请求参数不正确");
        }
        Role role = findById(model.getId());
        if (!role.getName().equals(model.getName()) && roleDao.existsByName(model.getName())) {
            throw new DoValidException("角色名称已存在");
        }
        role.setName(model.getName());
        role.setRemark(model.getRemark());

        roleDao.save(role);
    }

    public Page<Role> findAll(RoleGetVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            SpecificationBuilder<Role> builder = new SpecificationBuilder<>();
            Specification<Role> specification = builder.init(jsonObject)
                    .sContain("name", "name")
                    .aOrder("createTime")
                    .build();
            return roleDao.findAll(specification, builder.getPageable());
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public void deleteUser(String id, HttpServletRequest request) throws Exception {

        Role role = findById(id);
        List<RolePermission> rolePermissions = rolePermissionDao.findAllByRoleId(id);

        List<UserRole> userToRoles = userRoleDao.findAllByRoleId(id);
        if (userToRoles.size() > 0) {
            throw new Exception("该角色已被分配不可删除");
        }
        this.rolePermissionDao.deleteAll(rolePermissions);
        roleDao.deleteById(id);
    }

    private void moveRole(List<Role> roleList, String name) {
        Integer index = null;
        for (int i = 0; i < roleList.size(); i++) {
            if (name.equals(roleList.get(i).getName())) {
                index = i;
                break;
            }
        }
        if (index != null) {
            Role temp = roleList.get(index);
            Iterator<Role> it = roleList.iterator();
            while (it.hasNext()) {
                if (name.equals(it.next().getName())) {
                    it.remove();
                }
            }
            roleList.add(0, temp);
        }
    }

    public List<Role> findAllRoles() {
        Sort sort = new Sort(Sort.Direction.DESC, "createTime");
        return roleDao.findAll(sort);
    }

    public Role findById(String id) throws Exception {
        return roleDao.findById(id).orElseThrow(() -> new DoValidException("该角色不存在"));
    }

    public List<Role> findAll() {
        return roleDao.findAll();
    }
}
