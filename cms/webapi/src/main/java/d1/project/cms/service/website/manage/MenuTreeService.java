package d1.project.cms.service.website.manage;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.cms.dao.website.manage.MenuTreeDao;
import d1.project.cms.entity.website.manage.MenuTree;
import d1.project.cms.model.website.manage.MenuTreeGN;
import d1.project.cms.model.website.manage.MenuTreeGetVm;
import d1.project.cms.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @author all
 */
@Service
public class MenuTreeService {

    @Autowired
    private MenuTreeDao menuTreeDao;

    boolean existsByParentId(String parentId) {
        return menuTreeDao.existsByParentId(parentId);
    }

    public List<MenuTreeGetVm> findAllTree() {
        List<MenuTree> all = menuTreeDao.findAllOrderById();
        int allSize = all.size();
        List<MenuTreeGetVm> menus = new ArrayList<>(allSize);
        for (int i = 0; i < all.size(); i++) {
            // 一级菜单没有parentId
            if (StringUtils.isEmpty(all.get(i).getParentId())) {
                setMenuTree(all, i, menus);
            }
        }
        return menus;
    }

    public MenuTreeGetVm findByMenuId(String menuId) {
        List<MenuTreeGetVm> allTree = findAllTree();
        return getMenuTreeGetVm(menuId, allTree);
    }

    private MenuTreeGetVm getMenuTreeGetVm(String menuId, List<MenuTreeGetVm> allTree) {
        for (MenuTreeGetVm menuTreeGetVm : allTree) {
            if (menuId.equals(menuTreeGetVm.getId())) {
                return menuTreeGetVm;
            } else {
                List<MenuTreeGetVm> children = menuTreeGetVm.getChildren();
                if (children != null) {
                    MenuTreeGetVm menuTreeGetVm1 = getMenuTreeGetVm(menuId, children);
                    if (menuTreeGetVm1 != null) {
                        return menuTreeGetVm1;
                    }
                }
            }
        }
        return null;
    }


    private void setMenuTree(List<MenuTree> menus, Integer i, List<MenuTreeGetVm> childList) {
        MenuTreeGetVm mo = new MenuTreeGetVm();
        MenuTree menuTree = menus.get(i);
        String childId = menuTree.getId();
        mo.setId(childId);
        mo.setParentId(menuTree.getParentId());
        mo.setName(menuTree.getName());
        mo.setType(menuTree.getType());
        mo.setRoute(menuTree.getRoute());
        mo.setOrderNum(menuTree.getOrderNum());
        List<MenuTreeGetVm> child = findChild(menus, childId);
        mo.setChildren(child);
        mo.setLabel(menuTree.getName());
        childList.add(mo);
    }


    private List<MenuTreeGetVm> findChild(List<MenuTree> menus, String id) {
        List<MenuTreeGetVm> childList = new ArrayList<>();
        for (int i = 0; i < menus.size(); i++) {
            if (!StringUtils.isEmpty(menus.get(i).getParentId())) {
                if (menus.get(i).getParentId().equals(id)) {
                    setMenuTree(menus, i, childList);
                }
            }
        }
        if (childList.isEmpty()) {
            return null;
        }
        return childList;
    }

    public void insert(JSONObject jsonObject) throws Exception {
        MenuTree model = JSON.toJavaObject(jsonObject, MenuTree.class);
        model.setId(MyUtils.generate32Id());
        MyUtils.throwMsg(menuTreeDao.existsByOrderNumAndParentId(model.getOrderNum(), model.getParentId()), "同级别已存在相同排序");
        menuTreeDao.save(model);
    }

    public void update(JSONObject jsonObject) throws Exception {
        MenuTree model = JSON.toJavaObject(jsonObject, MenuTree.class);
        MyUtils.throwMsg(model == null, "请求参数不正确");
        MenuTree menuTree = menuTreeDao.findById(model.getId()).orElseThrow(() -> new Exception("未找到id=" + model.getId() + "的资源"));
        MenuTree byId = menuTreeDao.findById(model.getId()).orElseThrow(() -> new Exception("未找到要求改的内容"));
        if (!byId.getParentId().equals(model.getParentId()) || !byId.getOrderNum().equals(model.getOrderNum())) {
            MyUtils.throwMsg(menuTreeDao.existsByOrderNumAndParentId(model.getOrderNum(), model.getParentId()), "同级别已存在相同排序");
        }
        menuTreeDao.save(model);
    }


    public List<MenuTree> findAllById(List<String> ids) {
        return menuTreeDao.findAllById(ids);
    }

    public List<MenuTree> findAllOrderById() {
        return menuTreeDao.findAllOrderById();
    }

    public List<MenuTree> findOrderById(List<String> ids) {
        return menuTreeDao.findOrderById(ids);
    }

    public MenuTreeGN findByIdGn(String id) throws Exception {
        MenuTree byId = menuTreeDao.findById(id).orElseThrow(() -> new Exception("该id无法找到"));
        String parentId = byId.getParentId();
        MenuTreeGN menuTreeGN = new MenuTreeGN();
        BeanUtils.copyProperties(byId, menuTreeGN);

        if (!StringUtils.isEmpty(parentId)) {
            MenuTree byParentId = menuTreeDao.findById(parentId).orElseThrow(() -> new Exception("该id无法找到"));
            menuTreeGN.setParentName(byParentId.getName());
        }
        return menuTreeGN;
    }

    public void delete(String id) throws Exception {
        if (!menuTreeDao.existsById(id)) {
            throw new DoValidException("未找到id=" + id + "的资源");
        }
        menuTreeDao.deleteById(id);
    }


}
