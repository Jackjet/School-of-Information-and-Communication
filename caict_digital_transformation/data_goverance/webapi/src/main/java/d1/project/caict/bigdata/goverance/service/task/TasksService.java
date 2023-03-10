package d1.project.caict.bigdata.goverance.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.caict.bigdata.goverance.dao.task.GroupTaskDao;
import d1.project.caict.bigdata.goverance.dao.task.TasksDao;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.entity.rule.Rule;
import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import d1.project.caict.bigdata.goverance.entity.task.TaskLog;
import d1.project.caict.bigdata.goverance.entity.task.Tasks;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.datasource.JobDataSourceService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.service.rule.RuleLogService;
import d1.project.caict.bigdata.goverance.service.rule.RuleService;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import net.dcrun.component.http.HttpService;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.ValidationException;
import java.util.*;

/**
 * @author maoyy
 */

@Service
public class TasksService {
    private final TasksDao tasksDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;
    private final TaskLogService taskLogService;
    private final GroupTaskLogService groupTaskLogService;
    private final GroupTaskDao groupTaskDao;
    private final RuleLogService ruleLogService;
    private final RuleService ruleService;
    private final JobDataSourceService jobDataSourceService;
    private final WebAdminService webAdminService;

    public TasksService(TasksDao tasksDao, TokenService tokenService, OperationLogService operationLogService, TaskLogService taskLogService, GroupTaskLogService groupTaskLogService, GroupTaskDao groupTaskDao, RuleLogService ruleLogService, RuleService ruleService, JobDataSourceService jobDataSourceService, WebAdminService webAdminService) {
        this.tasksDao = tasksDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
        this.taskLogService = taskLogService;
        this.groupTaskLogService = groupTaskLogService;
        this.groupTaskDao = groupTaskDao;
        this.ruleLogService = ruleLogService;
        this.ruleService = ruleService;
        this.jobDataSourceService = jobDataSourceService;
        this.webAdminService = webAdminService;
    }


    /**
     * ??????????????????
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<Tasks> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Tasks> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("strategy", "strategy")
                .sEqual("status", "status")
                .sEqual("groupTaskId", "groupTaskId")
                .sContain("name", "name")
                .dOrder("createTime").build();

        return this.tasksDao.findAll(specification, builder.getPageable());
    }

    /**
     * ????????????????????????
     *
     * @param id
     * @return
     */
    public Optional<Tasks> findById(String id) {
        return tasksDao.findById(id);
    }

    /**
     * ??????????????????
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        //???????????????????????????
        Tasks groupTask = tasksDao.findByName(params.getString("name"));
        if (groupTask != null) {
            throw new ValidException("????????????????????????");
        }
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            Tasks entity = JSON.toJavaObject(params, Tasks.class);
            String header = entity.getHeader();
            if (!StringUtils.isEmpty(header)) {
                try {
                    JSON.parseObject(header);
                } catch (Exception e) {
                    throw new Exception("header???????????????json??????");
                }
            }
            String body = entity.getBody();
            if (!StringUtils.isEmpty(body)) {
                try {
                    JSON.parseObject(body);
                } catch (Exception e) {
                    throw new Exception("body???????????????json??????");
                }
            }
            entity.setId(MyUtils.generate32Id());
            entity.setStatus("??????");
            tokenService.updateCreateIdAndTime(request, entity);
            tasksDao.save(entity);
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * ??????????????????
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        Tasks entity = tasksDao.findById(id).orElseThrow(() -> new Exception("???????????????:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            Tasks model = MyUtils.model2Entity(params, Tasks.class);
            Tasks jobDataSource = MyUtils.copyProperties(model, entity);
            tokenService.updateUpdateIdAndTime(request, jobDataSource);
            String header = jobDataSource.getHeader();
            if (!StringUtils.isEmpty(header)) {
                try {
                    JSON.parseObject(header);
                } catch (Exception e) {
                    throw new Exception("header???????????????json??????");
                }
            }
            String body = jobDataSource.getBody();
            if (!StringUtils.isEmpty(body)) {
                try {
                    JSON.parseObject(body);
                } catch (Exception e) {
                    throw new Exception("body???????????????json??????");
                }
            }
            tasksDao.save(jobDataSource);
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * ??????????????????
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        Tasks entity = tasksDao.findById(id).orElseThrow(() -> new Exception("???????????????:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            tasksDao.delete(entity);
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????" + entity.getName(), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("?????????????????????", "??????????????????-????????????", "????????????" + entity.getName(), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * ??????????????????  ????????????????????????
     */
    public void updateStatus(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        Tasks entity = tasksDao.findById(id).orElseThrow(() -> new Exception("???????????????:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            if ("??????".equals(params.getString("status"))) {
                entity.setStatus("??????");
            } else {
                entity.setStatus("??????");
            }
            tokenService.updateUpdateIdAndTime(request, entity);
            tasksDao.save(entity);
            operationLogService.insert(new OperationLog("?????????????????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("????????????????????????????????????", "??????????????????-????????????", "????????????????????????:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * ???????????????Id,??????????????????
     *
     * @param groupTaskId ?????????id
     * @return ???????????????
     * @throws Exception
     */
    public List<Tasks> findByGroupTaskId(String groupTaskId) throws Exception {
        return tasksDao.findByGroupTaskId(groupTaskId);
    }

    /**
     * ????????????
     *
     * @param id id
     * @throws Exception
     */
    public void execute(String id, String currentWebadminUserId, String currentWebadminUserName, String groupTaskLogId) throws Exception {
//        String tokenByHttpServletRequest = MyUtils.getTokenByHttpServletRequest(request);
        Tasks entity = tasksDao.findById(id).orElseThrow(() -> new Exception("???????????????:" + id));

        String taskLogId = MyUtils.generate32Id();
        JSONObject jsonObject1 = new JSONObject();
        jsonObject1.put("id", taskLogId);
        jsonObject1.put("groupTaskId", entity.getGroupTaskId());
        jsonObject1.put("groupTaskName", entity.getGroupTaskName());
        jsonObject1.put("taskId", entity.getId());
        jsonObject1.put("taskName", entity.getName());
        jsonObject1.put("sourceTable", entity.getSourceTable());
        jsonObject1.put("targetTable", entity.getTargetTable());
        jsonObject1.put("strategy", entity.getStrategy());
        jsonObject1.put("groupTaskLogId", groupTaskLogId);
        taskLogService.insert(jsonObject1, currentWebadminUserId, currentWebadminUserName);

        //TODO ???????????????????????????????????????
        if (StringUtils.isEmpty(entity.getApi()) || StringUtils.isEmpty(entity.getMethod())) {
            return;
        }
        //??????HTTP??????,???????????????????????????
        if (!StringUtils.isEmpty(entity.getApi()) || !StringUtils.isEmpty(entity.getMethod())) {
            String api = entity.getApi();
            HttpService httpService = new HttpService();
            //?????????id????????????id,??????????????????????????????????????????????????????

            switch (entity.getMethod()) {
                case "GET":
                    Map<String, Object> bodyGet = getStringObjectMap(null, taskLogId, entity, groupTaskLogId);
                    httpService.get(api + entity.getUrlParams(), JSON.parseObject(entity.getHeader()), bodyGet);
                    break;
                case "POST":
                    Map<String, Object> bodyPost = getStringObjectMap(entity.getBody(), taskLogId, entity, groupTaskLogId);
                    httpService.post(api, JSON.parseObject(entity.getHeader()), bodyPost, "application/json");
                    break;
                case "PUT":
                    Map<String, Object> bodyPut = getStringObjectMap(entity.getBody(), taskLogId, entity, groupTaskLogId);
                    httpService.put(api, JSON.parseObject(entity.getHeader()), bodyPut, "application/json");
                    break;
                case "DELETE":
                    Map<String, Object> bodyDelete = getStringObjectMap(entity.getBody(), taskLogId, entity, groupTaskLogId);
                    httpService.delete(api, JSON.parseObject(entity.getHeader()), bodyDelete, "application/json");
                    break;
                default:
                    break;
            }
        }
    }

    private Map<String, Object> getStringObjectMap(String params, String taskLogId, Tasks entity, String groupTaskLogId) throws Exception {
        JobDataSource byId1 = jobDataSourceService.findById(entity.getDataSourceSourceId()).orElseThrow(() -> new Exception("????????????????????????"));
        JobDataSource byId = jobDataSourceService.findById(entity.getDataSourceTargetId()).orElseThrow(() -> new Exception("???????????????????????????"));

        Map<String, Object> body = JSON.parseObject(params);
        if (body == null) {
            body = new HashMap<>();
        }
        body.put("taskLogId", taskLogId);
        body.put("groupTaskId", entity.getGroupTaskId());
        body.put("groupTaskLogId", groupTaskLogId);
        body.put("taskId", entity.getId());
        body.put("isRule", "1");


        body.put("sourceJdbc", byId1.getJdbcServer());
        body.put("sourceJdbcUrl", byId1.getJdbcUrl());
        body.put("sourceUserName", byId1.getUserName());
        body.put("sourcePassword", byId1.getPassword());
        body.put("sourceTable", entity.getSourceTable());

        body.put("targetJdbc", byId.getJdbcServer());
        body.put("targetJdbcUrl", byId.getJdbcUrl());
        body.put("targetUserName", byId.getUserName());
        body.put("targetPassword", byId.getPassword());
        body.put("targetTable", entity.getTargetTable());
        String ruleId = entity.getRuleId();
        if (!StringUtils.isEmpty(ruleId)) {
            Optional<Rule> byId2 = ruleService.findById(ruleId);
            if (byId2.isPresent()) {
                Rule rule = byId2.get();
                body.put("ruleSql", rule.getSql());
            }
        }


        if (StringUtils.isEmpty(ruleId)) {
            body.put("isRule", "0");
        }
        return body;
    }

    /**
     * ??????????????????
     *
     * @param params
     * @throws Exception
     */
    public void taskCallBack(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("taskLogId");
        Date startTime = params.getDate("startTime");
        Date endTime = params.getDate("endTime");
        String status = params.getString("status");
        String callBackUrl = request.getRequestURL().toString();
        TaskLog taskLog = taskLogService.findById(id).orElseThrow(() -> new ValidException("???????????????????????????:" + id));

        //??????????????????????????????
        Tasks entity = tasksDao.findById(taskLog.getTaskId()).orElseThrow(() -> new ValidationException("?????????????????????:" + id));
        GroupTask byId1 = groupTaskDao.findById(entity.getGroupTaskId()).orElseThrow(() -> new ValidationException("???????????????????????????:" + entity.getRuleId()));
        Optional<WebAdminUser> byId = webAdminService.findById(StringUtils.isEmpty(byId1.getUpdateById()) ? byId1.getCreateById() : byId1.getUpdateById());

        if (!StringUtils.isEmpty(entity.getRuleId())) {
            Rule rule = ruleService.findById(entity.getRuleId()).orElseThrow(() -> new ValidationException("?????????????????????:" + entity.getRuleId()));
            //??????????????????
            JSONObject js = new JSONObject();
            js.put("ruleId", entity.getRuleId());
            js.put("ruleName", entity.getRuleName());
            js.put("taskId", taskLog.getTaskId());
            js.put("taskName", taskLog.getTaskName());
            js.put("templateId", rule.getTemplateId());
            js.put("templateName", rule.getTemplateName());
            js.put("startTime", startTime);
            js.put("endTime", endTime);
            js.put("status", status);
            js.put("checkCount", params.getInteger("checkCount"));
            js.put("errorCount", params.getInteger("errorCount"));
            js.put("logDesc", params.getString("logDesc"));
            js.put("ruleSql", params.getString("ruleSql"));

            if (byId.isPresent()) {
                WebAdminUser webAdminUser = byId.get();
                ruleLogService.insert(js, webAdminUser.getId(), webAdminUser.getName());
            }
        }

        JSONObject jTaskLog = new JSONObject();
        jTaskLog.put("taskLogId", id);
        jTaskLog.put("startTime", startTime);
        jTaskLog.put("endTime", endTime);
        jTaskLog.put("status", status);
        jTaskLog.put("callBackUrl", callBackUrl);
        jTaskLog.put("logDesc", params.getString("logDesc"));
        taskLogService.update(jTaskLog);

        String groupTaskLogId = taskLog.getGroupTaskLogId();
        if (!StringUtils.isEmpty(groupTaskLogId)) {
            long aLong = taskLogService.countByGroupTaskLogId(groupTaskLogId);
            long aLong1 = taskLogService.countByGroupTaskLogIdAndStatusIn(groupTaskLogId, "????????????", "????????????");
            if (aLong == aLong1) {
                long aLong2 = taskLogService.countByGroupTaskLogIdAndStatusIn(groupTaskLogId, "????????????", "????????????");
                //?????????????????????
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("id", params.getString("groupTaskLogId"));
                jsonObject.put("endTime", endTime);
                jsonObject.put("status", "????????????");
                if (aLong2 > 0) {
                    jsonObject.put("status", "????????????");
                }
                jsonObject.put("callBackUrl", callBackUrl);
                groupTaskLogService.update(jsonObject);

            }

        }
    }

    public boolean existsByRuleId(String ruleId) {
        return tasksDao.existsByRuleId(ruleId);
    }

}
