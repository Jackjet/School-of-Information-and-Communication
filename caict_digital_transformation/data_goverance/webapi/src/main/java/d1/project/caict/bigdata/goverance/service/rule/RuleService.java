package d1.project.caict.bigdata.goverance.service.rule;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.rule.RuleDao;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.rule.Rule;
import d1.project.caict.bigdata.goverance.entity.rule.RuleUpdateLog;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.TemplateService;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

@Service
public class RuleService {

    private final
    RuleDao dao;

    private final
    WebAdminService webAdminService;

    private final
    OperationLogService operationLogService;

    private final
    RuleUpdateLogService ruleUpdateLogService;

    private final
    TemplateService templateService;

    public RuleService(RuleDao dao, WebAdminService webAdminService, OperationLogService operationLogService, RuleUpdateLogService ruleUpdateLogService, TemplateService templateService) {
        this.dao = dao;
        this.webAdminService = webAdminService;
        this.operationLogService = operationLogService;
        this.ruleUpdateLogService = ruleUpdateLogService;
        this.templateService = templateService;
    }


    /**
     * 添加
     *
     * @param model   参数
     * @param request 客户端请求
     * @author Kikki  2020/7/5 14:10
     */
    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String name = model.getString("name");
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            Rule rule = MyUtils.model2Entity(model, Rule.class);

            if (dao.existsByName(rule.getName())) {
                throw new Exception("规则名称已存在");
            }
            rule.setCreateByName(currentWebadminUserEntity.getName());
            Rule app1 = MyUtils.initInsert(rule, currentWebadminUserEntity.getId());
            app1.setWorkOrderCount(0L);
            app1.setUpdateByName("");
            dao.save(app1);
            /*增加模板引用次数*/
            templateService.addReferencesCount(rule.getTemplateId());
            if (!StringUtils.isEmpty(rule.getUpdateMessage())) {
                ruleUpdateLogService.insert(() -> getRuleUpdateLog(currentWebadminUserEntity, app1, " ；操作类型：新增；"));
            }


            operationLogService.insert(new OperationLog("规则管理—添加", "质量检测规则—规则管理", "添加了规则:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—添加", "质量检测规则—规则管理", "添加了规则:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id id
     * @author Kikki  2020/7/5 14:10
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        Rule entityOptional = dao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String name = entityOptional.getName();
        try {

            dao.deleteById(id);
            templateService.lessReferencesCount(entityOptional.getTemplateId());
            operationLogService.insert(new OperationLog("规则管理—删除", "质量检测规则—规则管理", "删除了规则:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—删除", "质量检测规则—规则管理", "删除了规则:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 修改
     *
     * @param jsonObject 参数
     * @param request    客户端请求
     * @author Kikki  2020/7/5 14:10
     */
    public void update(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        Rule model = MyUtils.model2Entity(jsonObject, Rule.class);
        if (StringUtils.isEmpty(model.getUpdateMessage())) {
            model.setUpdateMessage(null);
        }
        String id = model.getId();
        Rule entityOptional = dao.findById(id).orElseThrow(() -> new ValidException("找不到记录：" + id));
        String name = entityOptional.getName();
        try {
            /* 更新前需验证 */
            if (StringUtils.isEmpty(id)) {
                throw new ValidException("id不能为空");
            }

            model.setUpdateByName(currentWebadminUserEntity.getName());
            /* 保存信息 */
            Rule app1 = MyUtils.initUpdate(model, currentWebadminUserEntity.getId());
            Rule app2 = MyUtils.copyProperties(app1, entityOptional);
            if (entityOptional.getTemplateId().equals(model.getTemplateId())) {
                /*增加模板引用次数*/
                templateService.addReferencesCount(app2.getTemplateId());
            }
            dao.save(app2);
            if (!StringUtils.isEmpty(model.getUpdateMessage())) {
                ruleUpdateLogService.insert(() -> getRuleUpdateLog(currentWebadminUserEntity, app1, " ；操作类型：编辑；"));
            }
            operationLogService.insert(new OperationLog("规则管理—修改", "质量检测规则—规则管理", "修改了规则:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());

        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—修改", "质量检测规则—规则管理", "修改了规则:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 根据id,修改关联工单数量
     *
     * @throws Exception
     */
    public void updateWorkOrderCount(JSONObject params, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String id = params.getString("ruleId");
        Rule rule = dao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        try {
            rule.setWorkOrderCount(rule.getWorkOrderCount() == null ? 1 : rule.getWorkOrderCount().longValue() + 1);
            dao.save(rule);
            operationLogService.insert(new OperationLog("规则管理—修改关联工单数量", "质量检测规则—规则管理", "修改了关联工单数量:" + rule.getName(), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—修改关联工单数量", "质量检测规则—规则管理", "修改了关联工单数量:" + rule.getName(), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    private RuleUpdateLog getRuleUpdateLog(WebAdminUser currentWebadminUserEntity, Rule app1, String s) {
        RuleUpdateLog ruleUpdateLog = new RuleUpdateLog();
        ruleUpdateLog.setLog("操作人：" + currentWebadminUserEntity.getName() + s + app1.getUpdateMessage());
        ruleUpdateLog.setRuleId(app1.getId());
        ruleUpdateLog.setId(MyUtils.generate32Id());
        ruleUpdateLog.setUpdateTime(Calendar.getInstance());
        return ruleUpdateLog;
    }

    public Page<Rule> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Rule> builder = new SpecificationBuilder<>();
        String dateFormat = "yyyy-MM-dd HH:mm:ss";
        Specification<Rule> specification = builder.init(params)
                .sContain("name", "name")
                .tBetween("createTime", "startTime", "endTime", dateFormat)
                .dOrder("createTime")
                .build();
        return dao.findAll(specification, builder.getPageable());
    }

    public Optional<Rule> findById(String id) {
        return dao.findById(id);
    }

    public Long countByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(Date createTime, Date createTime2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(createTime);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(createTime2);
        return dao.countByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(cal1, cal2);
    }

    public boolean existsByTemplateId(String templateId) {
        return dao.existsByTemplateId(templateId);
    }
}
