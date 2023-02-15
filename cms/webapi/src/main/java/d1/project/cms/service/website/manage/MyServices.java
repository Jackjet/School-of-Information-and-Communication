package d1.project.cms.service.website.manage;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.entity.website.manage.MenuTree;
import d1.project.cms.entity.website.manage.RolePermission;
import d1.project.cms.model.website.manage.MenuTreeGN;
import d1.project.cms.model.website.manage.MenuTreeObjectGN;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

/**
 * @author all
 */
@Service
public class MyServices {

    @Autowired
    TokenService tokenService;

    @Autowired
    private RolePermissionService rolePermissionService;

    @Autowired
    private MenuTreeService menuTreeService;


    /**
     * 我的功能树的路由
     *
     * @param request
     * @return
     */
    public List<String> getMyMenuTreeRoutes(HttpServletRequest request) {
        try {
            //当前用户Id
            JSONObject obj = tokenService.getUserByToken(request);

            List<MenuTree> menuTrees = this.getMenuTreeByUser(obj);

            List<String> routes = new ArrayList<>();

            for (MenuTree menutree : menuTrees) {
                if (StringUtils.isEmpty(menutree.getType()) && menutree.getType().toLowerCase().equals("action")) {
                    //逗号分隔的拆分
                    for (String route : menutree.getRoute().split(",")) {
                        if (!routes.contains(route)) {
                            routes.add(route);
                        }
                    }
                }
            }
            return routes;
        } catch (Exception e) {
            return null;
        }

    }

    public String getMyUserId(HttpServletRequest request) {
        JSONObject obj = tokenService.getUserByToken(request);
        return obj.getString("id");
    }

    /**
     * 我的功能树--前端结构拼装
     *
     * @param request
     * @return
     * @throws Exception
     */
    public List<MenuTreeObjectGN> getMyMenuTree(HttpServletRequest request) throws Exception {

        //当前用户Id
        JSONObject obj = tokenService.getUserByToken(request);

        List<MenuTree> menuTrees = this.getMenuTreeByUser(obj);

        return findTreeGN(menuTrees);
    }


    /**
     * 获取指定用戶权限
     */
    private List<MenuTree> getMenuTreeByUser(JSONObject currentUserObj) throws Exception {
        String userId = currentUserObj.getString("id");
        String deptId = currentUserObj.getString("deptId");

        List<MenuTree> menuTrees = new ArrayList<>();
        //admin默认全部权限
        if ("admin".equals(userId) || "root".equals(userId)) {
            menuTrees = menuTreeService.findAllOrderById();
        } else {
            List<String> menuTreeIds = new ArrayList<>();

            //角色权限
            List<RolePermission> rolePermissions = rolePermissionService.findRolePermissionByUserId(userId);

            for (RolePermission r : rolePermissions) {
                if (!menuTreeIds.contains(r.getMenuTreeId())) {
                    menuTreeIds.add(r.getMenuTreeId());
                }
            }

            if (!menuTreeIds.isEmpty()) {
                menuTrees = menuTreeService.findOrderById(menuTreeIds);
            }

        }
        return menuTrees;
    }

    private List<MenuTreeObjectGN> findTreeGN(List<MenuTree> all) {
        List<MenuTreeObjectGN> menus = new ArrayList<>();
        for (int i = 0; i < all.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(all.get(i).getParentId())) {
                setMenuTreeGN(all, i, menus);
            }
        }
        return menus;
    }

    private List<MenuTreeObjectGN> findChildGN(List<MenuTree> menus, String id) {
        List<MenuTreeObjectGN> childList = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            if (!StringUtils.isEmpty(menus.get(i).getParentId())) {
                if (menus.get(i).getParentId().equals(id)) {
                    setMenuTreeGN(menus, i, childList);
                }
            }
        }
        if (childList.size() == 0) {
            return new ArrayList<>();
        }
        return childList;
    }

    private void setMenuTreeGN(List<MenuTree> menus, Integer i, List<MenuTreeObjectGN> childList) {
        MenuTreeGN mo = new MenuTreeGN();
        MenuTreeObjectGN mog = new MenuTreeObjectGN();
        MenuTree menuTree = menus.get(i);
        String childId = menuTree.getId();
        mo.setId(childId);
        mo.setParentId(menuTree.getParentId());
        mo.setName(menuTree.getName());
        mo.setType(menuTree.getType());
        mo.setRoute(menuTree.getRoute());
        mo.setOrderNum(menuTree.getOrderNum());
        mo.setIcon(menuTree.getIcon());
        mog.setEntity(mo);
        List<MenuTreeObjectGN> child = findChildGN(menus, childId);
        mog.setChilds(child);
        childList.add(mog);
    }
}
