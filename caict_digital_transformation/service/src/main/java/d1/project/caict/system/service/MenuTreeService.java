package d1.project.caict.system.service;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.entity.BaseEntity;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.common.Constants;
import d1.project.caict.common.model.OperationLog;
import d1.project.caict.common.service.IOperationLogService;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.dao.MenuTreeDao;
import d1.project.caict.system.dao.RoleMenuTreeDao;
import d1.project.caict.system.entity.MenuTree;
import d1.project.caict.system.mapper.MenuTreeMapper;
import d1.project.caict.system.model.MenuTreeTree;
import d1.project.caict.system.model.vm.MenuTreeInsertVm;
import d1.project.caict.system.model.vm.MenuTreeUpdateSeqVm;
import d1.project.caict.system.model.vm.MenuTreeUpdateVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:40
 */
@Service
public class MenuTreeService {

    private final MenuTreeDao menuTreeDao;
    private final RoleMenuTreeDao roleMenuTreeDao;
    private final IOperationLogService iOperationLogService;

    private final MenuTreeMapper mapper;

    public MenuTreeService(MenuTreeDao menuTreeDao, RoleMenuTreeDao roleMenuTreeDao, IOperationLogService iOperationLogService) {
        this.menuTreeDao = menuTreeDao;
        this.mapper = Mappers.getMapper(MenuTreeMapper.class);
        this.roleMenuTreeDao = roleMenuTreeDao;
        this.iOperationLogService = iOperationLogService;
    }


    /**
     * 新增
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void insert(MenuTreeInsertVm model, HttpServletRequest request) throws DoValidException {


        MenuTree menuTree = mapper.dtoFormatIntoInsertEntity(model);
        TokenManager.getInstance().updateCreateIdAndTime(request, menuTree);
        menuTree.setId(BaseUtils.generate32Id());
        String parentId = menuTree.getParentId();
        /*等级从0开始*/
        menuTree.setLevel(0);
        /*等级中文信息从一开始*/
        menuTree.setLevelMsg(BaseUtils.toHanStr(1) + "级");
        menuTree.setIdLink(menuTree.getId());

        String parentId1 = StringUtils.isEmpty(parentId) ? "" : parentId;
        MenuTree menuTree1 = menuTreeDao.findFirstByParentIdOrderBySeqDesc(parentId1).orElse(null);
        menuTree.setSeq(0);
        if (menuTree1 != null) {
            menuTree.setSeq(menuTree1.getSeq() + 1);
        }

        if (!StringUtils.isEmpty(parentId)) {
            MenuTree parentIdMenu = menuTreeDao.findById(parentId).orElseThrow(() -> new DoValidException("父级菜单不存在"));
            int level = parentIdMenu.getLevel() + 1;
            /*等级从0开始*/
            menuTree.setLevel(level);
            /*等级中文信息从一开始*/
            menuTree.setLevelMsg(BaseUtils.toHanStr(level + 1) + "级");
            menuTree.setParentName(parentIdMenu.getName());
            menuTree.setIdLink(parentIdMenu.getIdLink() + "_" + menuTree.getId());
            menuTree.setRedirect("");
        } else {
            if (model.isChild()) {
                /*0菜单，1按钮，2新窗口，3内页，4内部功能页面*/
                if (model.getType() != 0) {
                    throw new DoValidException("该类型不允许有子菜单，请重新选择");
                }
            }
        }
        if (menuTree.getLevel() > 2) {
            throw new DoValidException("菜单最多可建三级菜单");
        }

        if (menuTreeDao.existsByNameAndParentId(menuTree.getName(), parentId1)) {
            throw new DoValidException("请重新输入，该菜单名称同层级下已存在");
        }
        menuTreeDao.save(menuTree);
        iOperationLogService.insert(new OperationLog("系统管理_菜单管理", "新增", "新增菜单" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }

    /**
     * 删除
     *
     * @param id id
     */
    @Transactional(rollbackFor = Exception.class)
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (Constants.CANNOT_DELETED_MENU_TREE.contains(id)) {
            throw new DoValidException("初始化菜单不可删除");
        }
        MenuTree menuTree = menuTreeDao.findById(id).orElseThrow(() -> new DoValidException("菜单不存在"));
        deleteBase(menuTree);
        menuTreeDao.deleteById(menuTree.getId());
        roleMenuTreeDao.deleteByMenuTreeId(menuTree.getId());
        List<MenuTree> byParentIdOrderBySeqAsc = menuTreeDao.findByParentIdOrderBySeqAsc(menuTree.getParentId());
        for (int i = 0; i < byParentIdOrderBySeqAsc.size(); i++) {
            byParentIdOrderBySeqAsc.get(i).setSeq(i);
        }
        menuTreeDao.saveAll(byParentIdOrderBySeqAsc);

        iOperationLogService.insert(new OperationLog("系统管理_菜单管理", "删除", "删除菜单" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }

    @Transactional(rollbackFor = Exception.class)
    void deleteBase(MenuTree menuTree) {
        for (MenuTree menuTreeEntity : menuTreeDao.findByParentId(menuTree.getId())) {
            menuTreeDao.deleteById(menuTreeEntity.getId());
            roleMenuTreeDao.deleteByMenuTreeId(menuTreeEntity.getId());
            deleteBase(menuTreeEntity);
        }
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAll(Collection<String> ids, HttpServletRequest request) throws DoValidException {
        for (String id : ids) {
            delete(id, request);
        }
    }

    /**
     * 更新
     *
     * @param model 传输模型
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(MenuTreeUpdateVm model, HttpServletRequest request) throws DoValidException {
        String id = model.getId();
        if (Constants.CANNOT_DELETED_ORGANIZATION.equals(id)) {
            throw new DoValidException("初始化菜单不可修改");
        }
        MenuTree menuTree = menuTreeDao.findById(id).orElseThrow(() -> new DoValidException("菜单不存在"));
        String parentId = menuTree.getParentId();
        if (!StringUtils.isEmpty(parentId)) {
            if (menuTreeDao.existsByNameAndParentIdAndIdNot(model.getName(), parentId, menuTree.getId())) {
                throw new DoValidException("请重新输入，该菜单名称同层级下已存在");
            }
        } else {
            if (model.isChild()) {
                /*0菜单，1按钮，2新窗口，3内页，4内部功能页面*/
                if (model.getType() != 0) {
                    throw new DoValidException("该类型不允许有子菜单，请重新选择");
                }
            }
        }

        if (!model.isChild()) {
            if (menuTreeDao.existsByParentId(id)) {
                throw new DoValidException("当前菜单下有子菜单,请删除后再操作");
            }
        }

        mapper.copyProperties(model, menuTree);
        TokenManager.getInstance().updateUpdateIdAndTime(request, menuTree);

        menuTreeDao.save(menuTree);

        iOperationLogService.insert(new OperationLog("系统管理_菜单管理", "编辑", "编辑菜单" + menuTree.getName(), JSON.toJSONString(menuTree), 1), request);
    }


    /**
     * 序列更新
     *
     * @param model 模型
     */
    public void updateSeq(MenuTreeUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        MenuTree menuTree = menuTreeDao.findById(model.getId()).orElseThrow(() -> new DoValidException("菜单不存在"));
        int seq = menuTree.getSeq();
        int i;
        /*动作:0->上移->1下移*/
        String action;
        String parentId = menuTree.getParentId();
        parentId = StringUtils.isEmpty(parentId) ? "" : menuTree.getParentId();
        if (model.getAction() == 0) {
            i = seq - 1;
            if (i < 0) {
                return;
            }

            MenuTree byParentIdAndSeq = menuTreeDao.findByParentIdAndSeq(parentId, i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() + 1);
            menuTreeDao.save(byParentIdAndSeq);
            action = "上移";
        } else {
            i = seq + 1;
            MenuTree byParentIdAndSeq = menuTreeDao.findByParentIdAndSeq(parentId, i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() - 1);
            menuTreeDao.save(byParentIdAndSeq);
            action = "下移";
        }

        menuTree.setSeq(i);
        menuTreeDao.save(menuTree);

        iOperationLogService.insert(new OperationLog("系统管理_菜单管理", "编辑序列", "编辑序列" + menuTree.getName() + action, JSON.toJSONString(menuTree), 1), request);
    }

    /**
     * 查询所有
     */
    public List<MenuTreeTree> findAllFormatIntoVm() {
        List<MenuTree> seq = menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        return getMenuTreeTreeVm(seq, false);
    }

    /**
     * 查询所有
     */
    public List<MenuTree> findAll() {
        return menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
    }

    public List<String> findAllAsLongAsId() {
        return menuTreeDao.findAll(Sort.by(Sort.Direction.ASC, "seq")).stream().filter(menuTreeEntity -> !StringUtils.isEmpty(menuTreeEntity.getParentId())).map(BaseEntity::getId).collect(Collectors.toList());
    }

    /**
     * 找到所有没有子集的菜单ID
     */
    public List<String> findAllNoChildrenAsLongAsId(List<String> collect) {
        List<String> menuTreeIds = new ArrayList<>();
        List<MenuTreeTree> allByIdIn = findAllByIdIn(collect, false);
        recursionNoChildrenTheAsLongAsId(menuTreeIds, allByIdIn);
        return menuTreeIds;
    }

    /**
     * 递归没有子集的菜单，只要ID
     */
    private void recursionNoChildrenTheAsLongAsId(List<String> menuTreeIds, List<MenuTreeTree> allByIdIn) {
        for (MenuTreeTree menuTreeTree : allByIdIn) {
            List<MenuTreeTree> children = menuTreeTree.getChildren();
            if (children.isEmpty()) {
                menuTreeIds.add(menuTreeTree.getId());
            } else {
                recursionNoChildrenTheAsLongAsId(menuTreeIds, children);
            }

        }
    }


    /**
     * 查询id
     */
    public MenuTreeTree find(String id) {
        List<MenuTree> byLevelAsc = menuTreeDao.findByIdLinkLikeOrderBySeqAsc(id);
        return getOrganizationTreeVm(byLevelAsc);
    }

    /**
     * 根据菜单id列表获取菜单树
     *
     * @param id        id列表
     * @param isSidebar 是否是侧边栏
     */
    public List<MenuTreeTree> findAllByIdIn(Collection<String> id, boolean isSidebar) {
        List<MenuTree> seq;
        if(isSidebar){
            seq = menuTreeDao.findAllByIdInAndTypeInOrderBySeqAsc(id, Arrays.asList(0,2,3));
        }else {
            seq = menuTreeDao.findAllByIdInOrderBySeqAsc(id);
        }
        return getMenuTreeTreeVm(seq, isSidebar);
    }

    public List<MenuTree> findAllByIdInOrderBySeqAsc(Collection<String> id) {
        return menuTreeDao.findAllByIdInOrderBySeqAsc(id);
    }


    /**
     * 根据菜单列表获取菜单树
     *
     * @param byLevelAsc 组织结构列表
     */
    private MenuTreeTree getOrganizationTreeVm(List<MenuTree> byLevelAsc) {
        MenuTreeTree menuTreeTreeVm = new MenuTreeTree();
        for (MenuTree treeTreeVm : byLevelAsc) {
            if (StringUtils.isEmpty(treeTreeVm.getParentId())) {
                menuTreeTreeVm = mapper.entityFormatIntoDtoTree(treeTreeVm);
                recursion(byLevelAsc, menuTreeTreeVm, false);
            }
        }
        return menuTreeTreeVm;
    }

    /**
     * 根据菜单列表获取菜单列表树
     *
     * @param byLevelAsc 菜单列表树
     */
    private List<MenuTreeTree> getMenuTreeTreeVm(List<MenuTree> byLevelAsc, boolean isSidebar) {
        List<MenuTreeTree> menuTreeTreeVms = new ArrayList<>();
        for (MenuTree menuTree : byLevelAsc) {
            if (StringUtils.isEmpty(menuTree.getParentId())) {
                MenuTreeTree menuTreeTreeVm = mapper.entityFormatIntoDtoTree(menuTree);
//                menuTreeTreeVm.setRedirect(menuTreeTreeVm.getPath());
                recursion(byLevelAsc, menuTreeTreeVm, isSidebar);
                menuTreeTreeVms.add(menuTreeTreeVm);
            }
        }
        return menuTreeTreeVms;
    }

    /**
     * 递归
     *
     * @param menuTreeList   菜单列表
     * @param menuTreeTreeVm 菜单树
     */
    private void recursion(List<MenuTree> menuTreeList, MenuTreeTree menuTreeTreeVm, boolean isSidebar) {

        for (MenuTree organization1 : menuTreeList) {
            if (menuTreeTreeVm.getId().equals(organization1.getParentId())) {
                List<MenuTreeTree> children = menuTreeTreeVm.getChildren();
                MenuTreeTree organizationTreeVm2 = mapper.entityFormatIntoDtoTree(organization1);
                if (isSidebar) {
                    if (organizationTreeVm2.getSeq() == 0) {
                        menuTreeTreeVm.setRedirect(menuTreeTreeVm.getPath() + "/" + organizationTreeVm2.getPath());
                    }
                    organizationTreeVm2.setPath(menuTreeTreeVm.getPath() + "/" + organizationTreeVm2.getPath());
                }
                children.add(organizationTreeVm2);
                recursion(menuTreeList, organizationTreeVm2, isSidebar);
            }
        }
        if (isSidebar) {
            if (StringUtils.isEmpty(menuTreeTreeVm.getRedirect())) {
                menuTreeTreeVm.setRedirect(menuTreeTreeVm.getPath());
            }
        }
    }


}
