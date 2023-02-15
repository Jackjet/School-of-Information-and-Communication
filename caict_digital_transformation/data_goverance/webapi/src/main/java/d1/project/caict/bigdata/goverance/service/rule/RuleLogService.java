package d1.project.caict.bigdata.goverance.service.rule;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.rule.RuleLogDao;
import d1.project.caict.bigdata.goverance.entity.rule.RuleLog;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class RuleLogService {
    private final
    RuleLogDao dao;
    private final OperationLogService operationLogService;
    final
    WebAdminService webAdminService;

    @Autowired
    public RuleLogService(RuleLogDao dao, OperationLogService operationLogService, WebAdminService webAdminService) {
        this.dao = dao;
        this.operationLogService = operationLogService;
        this.webAdminService = webAdminService;
    }

    public Page<RuleLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<RuleLog> builder = new SpecificationBuilder<>();
        Specification<RuleLog> specification = builder.init(params)
                .sContain("ruleName", "ruleName")
                .dOrder("startTime")
                .build();
        return dao.findAll(specification,builder.getPageable());
    }

    /**
     * 查询规则日志详细信息
     *
     * @param id
     * @return
     */
    public Optional<RuleLog> findById(String id) {
        return dao.findById(id);
    }

    /**
     * 检验折线图
     *
     * @param createTime  开始时间
     * @param createTime2 结束时间
     */
    public Map<String, Object> countGroupMonthByStartTime(Date createTime, Date createTime2) throws Exception {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(createTime);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(createTime2);

        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> monthBetween = MyUtils.getMonthBetween(simpleDateFormat.format(createTime), simpleDateFormat.format(createTime2));
        List<Map<String, Long>> maps = dao.countGroupMonthByStartTime(cal1, cal2);
        maps.forEach(stringObjectMap -> {
            Object name = stringObjectMap.get("name");
            Long value1 = stringObjectMap.get("value1");
            Long value2 = stringObjectMap.get("value2");
            JSONObject stringMap = new JSONObject(2);
            stringMap.put("value1", value1);
            stringMap.put("value2", value2);
            monthBetween.replace(String.valueOf(name), stringMap);
        });

        List<String> month = new ArrayList<>();
        List<Object> checkCountCount = new ArrayList<>();
        List<Object> errorCountCount = new ArrayList<>();
        monthBetween.forEach((s, o) -> {
            month.add(s);
            if (o instanceof JSONObject) {
                JSONObject o1 = (JSONObject) o;
                checkCountCount.add(o1.get("value1"));
                errorCountCount.add(o1.get("value2"));
            } else {
                checkCountCount.add(0);
                errorCountCount.add(0);
            }
        });
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("month", month);
        objectMap.put("checkCountCount", checkCountCount);
        objectMap.put("errorCountCount", errorCountCount);
        return objectMap;
    }


    public void insert(JSONObject params, String currentWebadminUserId,String currentWebadminUserName) throws Exception {
//        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            RuleLog ruleLog = new RuleLog();
            ruleLog.setId(MyUtils.generate32Id());
            ruleLog.setRuleId(params.getString("ruleId"));
            ruleLog.setRuleName(params.getString("ruleName"));
            ruleLog.setTaskId(params.getString("taskId"));
            ruleLog.setTaskName(params.getString("taskName"));
            ruleLog.setTemplateId(params.getString("templateId"));
            ruleLog.setTemplateName(params.getString("templateName"));
            Date startTime = params.getDate("startTime");
            Calendar cal1 = Calendar.getInstance();
            cal1.setTime(startTime);
            ruleLog.setStartTime(cal1);
            Date endTime = params.getDate("endTime");
            Calendar cal2 = Calendar.getInstance();
            cal2.setTime(endTime);
            ruleLog.setEndTime(cal2);
            ruleLog.setStatus(params.getString("status"));
            ruleLog.setCheckCount(params.getLong("checkCount"));
            ruleLog.setErrorCount(params.getLong("errorCount"));
            ruleLog.setLogDesc(params.getString("logDesc"));
            ruleLog.setRuleSql(params.getString("ruleSql"));
            this.dao.save(ruleLog);
            operationLogService.insert(new OperationLog("规则日志—添加", "质量检查规则-规则日志", "添加规则日志:" + params.getString("name"), "1"), currentWebadminUserId,currentWebadminUserName);
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则日志—添加", "质量检查规则-规则日志", "添加规则日志:" + params.getString("name"), "0"), currentWebadminUserId,currentWebadminUserName);
            throw new ValidException(e.getMessage());
        }
    }

    public Map<String, Long> sumByStartTime(Date createTime, Date createTime2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(createTime);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(createTime2);
        return dao.sumByStartTime(cal1, cal2);

    }
}
