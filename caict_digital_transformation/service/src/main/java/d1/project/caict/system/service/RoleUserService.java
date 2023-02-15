package d1.project.caict.system.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.dao.RoleUserDao;
import d1.project.caict.system.entity.RoleUser;
import d1.project.caict.system.model.MenuTreeTree;
import d1.project.caict.system.model.vm.RoleUserFindRoleIdVm;
import d1.project.caict.system.model.vm.RoleUserMenuTreeVm;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 10:11
 */

@Service
public class RoleUserService {

    private final RoleMenuTreeService roleMenuTreeService;

    private final RoleUserDao roleUserDao;


    public RoleUserService(RoleUserDao roleUserDao, RoleMenuTreeService roleMenuTreeService) {
        this.roleUserDao = roleUserDao;
        this.roleMenuTreeService = roleMenuTreeService;
    }

    public void insert(RoleUser model, HttpServletRequest request) throws DoValidException {
        TokenManager.getInstance().updateCreateIdAndTime(request, model);
        model.setId(BaseUtils.generate32Id());
        roleUserDao.save(model);
    }

    void deleteByUserId(String userId) {
        roleUserDao.deleteByUserId(userId);
    }

    void deleteByUserIdIn(Collection<String> userId) {
        roleUserDao.deleteByUserIdIn(userId);
    }

    void deleteByRoleId(String roleId) {
        roleUserDao.deleteByRoleId(roleId);
    }

    public Page<RoleUser> findRoleId(RoleUserFindRoleIdVm model) throws Exception {
        SpecificationBuilder<RoleUser> builder = new SpecificationBuilder<>();
        Specification<RoleUser> specification = builder.init((JSONObject) JSON.toJSON(model))
                .sContain("userName", "userName")
                .sEqual("roleId", "roleId")
                .sContain("userAccount", "userAccount")
                .sContain("userPhone", "userPhone")
                .sContain("organizationName", "organizationName")
                .dOrder("createTime")
                .build();
        return roleUserDao.findAll(specification, builder.getPageable());
    }

    public List<RoleUserMenuTreeVm> findCurrentUser(HttpServletRequest request) throws DoValidException {
        List<RoleUserMenuTreeVm> menuTreeVms = new ArrayList<>();
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<RoleUser> byUserId = roleUserDao.findByUserId(id);
        for (RoleUser roleUser : byUserId) {
            List<MenuTreeTree> allByRoleId = roleMenuTreeService.findAllByRoleId(roleUser.getRoleId(), false);
            RoleUserMenuTreeVm roleUserMenuTreeVm = new RoleUserMenuTreeVm(roleUser, allByRoleId);
            menuTreeVms.add(roleUserMenuTreeVm);
        }

        return menuTreeVms;
    }

    public List<Map<String, Map<String, List<String>>>> findCurrentUserButton(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<String> roles = roleUserDao.findByUserId(id).stream().map(RoleUser::getRoleId).collect(Collectors.toList());
        return roleMenuTreeService.findAllByRoleIdInButton(roles);
    }

    public List<RoleUserMenuTreeVm> findCurrentUserSidebar(HttpServletRequest request) throws DoValidException {
        JSONObject userByToken1 = TokenManager.getInstance().getUserByToken(request);
        System.out.println(userByToken1.toJSONString());

        List<RoleUserMenuTreeVm> menuTreeVms = new ArrayList<>();
        JSONObject userByToken = TokenManager.getInstance().getUserByToken(request);
        String id = userByToken.getString("id");
        List<RoleUser> byUserId = roleUserDao.findByUserId(id);
        for (RoleUser roleUser : byUserId) {
            List<MenuTreeTree> allByRoleId = roleMenuTreeService.findAllByRoleId(roleUser.getRoleId(), true);
            RoleUserMenuTreeVm roleUserMenuTreeVm = new RoleUserMenuTreeVm(roleUser, allByRoleId);
            menuTreeVms.add(roleUserMenuTreeVm);
        }
        return menuTreeVms;
    }


    public List<RoleUser> findByUserId(String userId) {
        return roleUserDao.findByUserId(userId);
    }

    public List<RoleUser> findByRoleId(String roleId) {
        return roleUserDao.findByRoleId(roleId);
    }

    boolean existsByRoleId(String roleId) {
        return roleUserDao.existsByRoleId(roleId);
    }

    public void saveAll(Iterable<RoleUser> entities) {
        roleUserDao.saveAll(entities);
    }

}
