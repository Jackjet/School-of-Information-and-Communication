package d1.project.caict.system.service;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.common.Constants;
import d1.project.caict.common.model.OperationLog;
import d1.project.caict.common.service.IOperationLogService;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.dao.OrganizationDao;
import d1.project.caict.system.dao.WebAdminUserDao;
import d1.project.caict.system.entity.Organization;
import d1.project.caict.system.entity.WebAdminUser;
import d1.project.caict.system.mapper.OrganizationMapper;
import d1.project.caict.system.model.OrganizationTree;
import d1.project.caict.system.model.vm.OrganizationInsertVm;
import d1.project.caict.system.model.vm.OrganizationUpdateSeqVm;
import d1.project.caict.system.model.vm.OrganizationUpdateVm;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 组织机构
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:34
 */
@Service
public class OrganizationService {

    private final OrganizationDao organizationDao;
    private final WebAdminUserDao webAdminUserDao;

    private final IOperationLogService iOperationLogService;
    private final OrganizationUserService organizationUserService;

    private final OrganizationMapper mapper;

    public OrganizationService(OrganizationDao organizationDao, IOperationLogService iOperationLogService, OrganizationUserService organizationUserService, WebAdminUserDao webAdminUserDao) {
        this.organizationDao = organizationDao;
        this.iOperationLogService = iOperationLogService;
        this.mapper = Mappers.getMapper(OrganizationMapper.class);
        this.organizationUserService = organizationUserService;
        this.webAdminUserDao = webAdminUserDao;
    }

    /**
     * 新增
     */
    public void insert(OrganizationInsertVm model, HttpServletRequest request) throws DoValidException {

        /*映射类型与补齐信息*/
        Organization organization = mapper.dtoFormatIntoInsertEntity(model);
        TokenManager.getInstance().updateCreateIdAndTime(request, organization);
        String id = BaseUtils.generate32Id();
        String parentId = organization.getParentId();
        organization.setId(id);
        /*寻找父级*/
        Organization organization1 = organizationDao.findById(parentId).orElseThrow(() -> new DoValidException("父级不存在"));
        int i = organization1.getLevel() + 1;
        /*寻找父级最后一个序列默认是第一个*/
        Organization organization2 = organizationDao.findFirstByParentIdOrderBySeqDesc(StringUtils.isEmpty(parentId) ? "" : parentId).orElse(null);
        organization.setSeq(0);
        if (organization2 != null) {
            organization.setSeq(organization2.getSeq() + 1);
        }
        organization.setLevel(i);
        organization.setLevelMsg(BaseUtils.toHanStr(i + 1) + "级");
        organization.setIdLink(organization1.getIdLink() + "_" + id);
        if (organizationDao.existsByName(model.getName())) {
            throw new DoValidException("请重新输入，该组织机构名称已存在");
        }
        organizationDao.save(organization);
        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "新增", "新增组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 删除
     */
    public void delete(String id, HttpServletRequest request) throws DoValidException {
        if (Constants.CANNOT_DELETED_ORGANIZATION.equals(id)) {
            throw new DoValidException("初始化组织机构不可删除");
        }
        Organization organization = find(id).orElseThrow(() -> new DoValidException("组织机构不存在"));
        List<String> collect = organizationDao.findByIdLinkLike(organization.getIdLink() + "%").stream().map(Organization::getId).collect(Collectors.toList());
        if (organizationUserService.existsByOrganizationIdIn(collect)) {
            throw new DoValidException("请移除该组织机构下全部用户再执行删除");
        }

        organizationDao.deleteById(id);
        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "删除", "删除组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 更新
     */
    public void update(OrganizationUpdateVm model, HttpServletRequest request) throws DoValidException {

        String oldName;
        Organization organization = organizationDao.findById(model.getId()).orElseThrow(() -> new DoValidException("组织机构不存在"));
        oldName = organization.getName();
        mapper.copyProperties(model, organization);
        TokenManager.getInstance().updateUpdateIdAndTime(request, organization);

        if (organizationDao.existsByNameAndIdNot(organization.getName(), organization.getId())) {
            throw new DoValidException("请重新输入，该组织机构名称已存在");
        }
        organizationDao.save(organization);

        if (!oldName.equals(organization.getName())) {
            List<WebAdminUser> collect = webAdminUserDao.findByOrganizationNameLike(oldName + "%").stream().peek(webAdminUser -> {
                String[] split = webAdminUser.getOrganizationName().split(",");
                List<String> strings = new ArrayList<>();
                for (String s : split) {
                    if (s.equals(oldName)) {
                        strings.add(organization.getName());
                        continue;
                    }
                    strings.add(s);
                }
                webAdminUser.setOrganizationName(String.join(",", strings));
            }).collect(Collectors.toList());
            webAdminUserDao.saveAll(collect);
        }

        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "编辑", "编辑组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 序列更新
     *
     * @param model 模型
     */
    public void updateSeq(OrganizationUpdateSeqVm model, HttpServletRequest request) throws DoValidException {
        Organization organization = organizationDao.findById(model.getId()).orElseThrow(() -> new DoValidException("组织机构不存在"));
        int seq = organization.getSeq();
        int i;
        /*动作:0->1上移->下移*/
        if (model.getAction() == 0) {
            i = seq - 1;
            if (i < 0) {
                return;
            }

            Organization byParentIdAndSeq = organizationDao.findByParentIdAndSeq(organization.getParentId(), i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() + 1);
            organizationDao.save(byParentIdAndSeq);

        } else {
            i = seq + 1;
            Organization byParentIdAndSeq = organizationDao.findByParentIdAndSeq(organization.getParentId(), i).orElse(null);
            if (byParentIdAndSeq == null) {
                return;
            }
            byParentIdAndSeq.setSeq(byParentIdAndSeq.getSeq() - 1);
            organizationDao.save(byParentIdAndSeq);

        }

        organization.setSeq(i);
        organizationDao.save(organization);

        iOperationLogService.insert(new OperationLog("系统管理_组织机构", "编辑序列", "编辑序列组织机构" + organization.getName(), JSON.toJSONString(organization), 1), request);
    }

    /**
     * 查询所有
     */
    public OrganizationTree findAll() {
        List<Organization> seq = organizationDao.findAll(Sort.by(Sort.Direction.ASC, "seq"));
        return getOrganizationTreeVm(seq);
    }

    public List<Organization> findAllList() {
        return organizationDao.findAll();
    }

    /**
     * 根据id查询组织结构树
     */
    public OrganizationTree findAllById(String id) {
        List<Organization> byLevelAsc = organizationDao.findByIdLinkLikeOrderBySeqAsc(id);
        return getOrganizationTreeVm(byLevelAsc);
    }

    /**
     * 详情
     */
    public Optional<Organization> find(String id) {
        return organizationDao.findById(id);
    }

    /**
     * 根据组织结构列表获取属性结构
     *
     * @param byLevelAsc 组织结构列表
     */
    private OrganizationTree getOrganizationTreeVm(List<Organization> byLevelAsc) {
        OrganizationTree organizationTreeVm = new OrganizationTree();
        for (Organization organization : byLevelAsc) {
            if (StringUtils.isEmpty(organization.getParentId())) {
                organizationTreeVm = mapper.entityFormatIntoDtoTree(organization);
                recursion(byLevelAsc, organizationTreeVm);
            }
        }
        return organizationTreeVm;
    }

    /**
     * 递归
     *
     * @param organizationList    组织机构列表
     * @param organizationTreeVm1 组织机构树
     */
    private void recursion(List<Organization> organizationList, OrganizationTree organizationTreeVm1) {
        for (Organization organization1 : organizationList) {
            if (organizationTreeVm1.getId().equals(organization1.getParentId())) {
                List<OrganizationTree> children = organizationTreeVm1.getChildren();
                OrganizationTree organizationTreeVm2 = mapper.entityFormatIntoDtoTree(organization1);
                children.add(organizationTreeVm2);
                recursion(organizationList, organizationTreeVm2);
            }
        }
    }

}
