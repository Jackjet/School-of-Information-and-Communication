package d1.project.caict.system.service;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.common.model.OperationLog;
import d1.project.caict.common.service.IOperationLogService;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.dao.RoleDao;
import d1.project.caict.system.dao.RoleMenuTreeDao;
import d1.project.caict.system.entity.MenuTree;
import d1.project.caict.system.entity.Role;
import d1.project.caict.system.entity.RoleMenuTree;
import d1.project.caict.system.model.MenuTreeTree;
import d1.project.caict.system.model.vm.RoleMenuTreeInsertVm;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 19:04
 */
@Service
public class RoleMenuTreeService {

    private final RoleDao roleDao;

    private final RoleMenuTreeDao roleMenuTreeDao;

    private final MenuTreeService menuTreeService;

    private final IOperationLogService iOperationLogService;

    public RoleMenuTreeService(RoleMenuTreeDao roleMenuTreeDao, MenuTreeService menuTreeService, IOperationLogService iOperationLogService, RoleDao roleDao) {
        this.roleMenuTreeDao = roleMenuTreeDao;
        this.menuTreeService = menuTreeService;
        this.iOperationLogService = iOperationLogService;
        this.roleDao = roleDao;
    }


    /**
     * 新增并替换
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(RoleMenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {
        //整体替换，删除原有数据
        String roleId = model.getRoleId();
        Role role = roleDao.findById(roleId).orElseThrow(() -> new DoValidException("角色不存在"));
        roleMenuTreeDao.deleteAllByRoleId(roleId);

        List<RoleMenuTree> roleMenuTrees = new ArrayList<>();
        for (String menuTreeId : model.getMenuTreeIds()) {
            RoleMenuTree roleMenuTree = new RoleMenuTree(roleId, menuTreeId);
            TokenManager.getInstance().updateCreateIdAndTime(request, roleMenuTree);
            roleMenuTree.setId(BaseUtils.generate32Id());
            roleMenuTrees.add(roleMenuTree);
        }

        roleMenuTreeDao.saveAll(roleMenuTrees);
        iOperationLogService.insert(new OperationLog("系统管理_角色权限", "分配权限", "分配角色" + role.getName() + "权限", JSON.toJSONString(role), 1), request);
    }

    /**
     * 根据角色id获取菜单树
     *
     * @param roleId    角色id
     * @param isSidebar 是否是侧边栏
     */
    public List<MenuTreeTree> findAllByRoleId(String roleId, boolean isSidebar) {
        Set<String> collect;
        if ("admin".equals(roleId)) {
            collect = menuTreeService.findAll().stream().map(MenuTree::getId).collect(Collectors.toSet());
        } else {
            collect = roleMenuTreeDao.findByRoleId(roleId).stream().map(RoleMenuTree::getMenuTreeId).collect(Collectors.toSet());
        }
        return  menuTreeService.findAllByIdIn(collect, isSidebar);
    }

    /**
     * 根据角色id获取菜单树按钮
     *
     * @param rolesIds 角色id列表
     */
    public List<Map<String, Map<String, List<String>>>> findAllByRoleIdInButton(List<String> rolesIds) {
        Set<String> collect = roleMenuTreeDao.findByRoleIdIn(rolesIds).stream().map(RoleMenuTree::getMenuTreeId).collect(Collectors.toSet());
        List<MenuTree> allByIdInOrderBySeqAsc;
        if (rolesIds.contains("admin")) {
            allByIdInOrderBySeqAsc = menuTreeService.findAll();
        } else {
            allByIdInOrderBySeqAsc = menuTreeService.findAllByIdInOrderBySeqAsc(collect);
        }

        List<Map<String, Map<String, List<String>>>> strings = new ArrayList<>();

        for (MenuTree menuTree : allByIdInOrderBySeqAsc) {
            /*0菜单，1按钮，2新窗口，3内页，4内部功能页面*/
            if (0 != menuTree.getType()) {
                continue;
            }

            Map<String, Map<String, List<String>>> objectMap = new HashMap<>(3);
            for (MenuTree menuTree1 : allByIdInOrderBySeqAsc) {
                if (1 == menuTree1.getType() && menuTree.getId().equals(menuTree1.getParentId())) {
                    Map<String, List<String>> o = objectMap.get(menuTree.getPath());
                    if (o == null) {
                        Map<String, List<String>> value = new HashMap<>(1);
                        value.put(menuTree1.getPath(), rolesIds);
                        objectMap.put(menuTree.getPath(), value);
                        continue;
                    }

                    o.put(menuTree1.getPath(), rolesIds);
                }
            }
            if (!objectMap.isEmpty()) {
                strings.add(objectMap);
            }

        }

        return strings;
    }

    /**
     * 根据角色id获取已选菜单树
     *
     * @param roleId 角色id
     */
    public List<String> findAllRoleIdChoose(String roleId) {
        if ("admin".equals(roleId)) {
            return menuTreeService.findAllAsLongAsId();
        }
        List<String> collect = roleMenuTreeDao.findByRoleId(roleId).stream().map(RoleMenuTree::getMenuTreeId).collect(Collectors.toList());
        return menuTreeService.findAllNoChildrenAsLongAsId(collect);
    }

}
