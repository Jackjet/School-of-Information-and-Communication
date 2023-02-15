package d1.project.caict.bigdata.goverance.service.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.caict.bigdata.goverance.configuration.QuartzManager;
import d1.project.caict.bigdata.goverance.configuration.TaskJob;
import d1.project.caict.bigdata.goverance.dao.task.GroupTaskDao;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import d1.project.caict.bigdata.goverance.entity.task.GroupTaskLog;
import d1.project.caict.bigdata.goverance.entity.task.Tasks;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.quartz.CronExpression;
import org.quartz.JobDataMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.List;
import java.util.Optional;

/**
 * @author maoyy
 */

@Service
public class GroupTaskService {
    private final GroupTaskDao groupTaskDao;
    private final TokenService tokenService;
    private final OperationLogService operationLogService;
    private final TasksService tasksService;
    private final GroupTaskLogService groupTaskLogService;
    private final WebAdminService webAdminService;

    @Autowired
    public GroupTaskService(GroupTaskDao groupTaskDao, TokenService tokenService, OperationLogService operationLogService,
                            TasksService tasksService, GroupTaskLogService groupTaskLogService, WebAdminService webAdminService) {
        this.groupTaskDao = groupTaskDao;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
        this.tasksService = tasksService;
        this.groupTaskLogService = groupTaskLogService;
        this.webAdminService = webAdminService;
    }

    /**
     * 查询任务组信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<GroupTask> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<GroupTask> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("sourceCatalog", "sourceCatalog")
                .sEqual("dataSourceSourceId", "dataSourceSourceId")
                .sEqual("targetCatalog", "targetCatalog")
                .sEqual("dataSourceTargetId", "dataSourceTargetId")
                .sEqual("operationMode", "operationMode")
                .sEqual("status", "status")
                .sContain("name", "name")
                .dOrder("createTime").build();

        return this.groupTaskDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询任务组详细信息
     *
     * @param id
     * @return
     */
    public Optional<GroupTask> findById(String id) {
        return groupTaskDao.findById(id);
    }

    /**
     * 添加任务组数据
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        //任务组名称不能重复
        GroupTask groupTask = groupTaskDao.findByName(params.getString("name"));
        if (groupTask != null) {
            throw new ValidException("任务组名称不能重复");
        }
        try {
            GroupTask entity = JSON.toJavaObject(params, GroupTask.class);
            entity.setId(MyUtils.generate32Id());
            entity.setStatus("已上线");
            tokenService.updateCreateIdAndTime(request, entity);
            /*运行模式,手动模式、周期调度*/
            if ("周期调度".equals(entity.getOperationMode())) {
                /*调度周期*/
                if (MyUtils.isEmpty(entity.getScheduleCycle())) {
                    throw new ValidException("调度周期不可为空");
                }
                if (!CronExpression.isValidExpression(entity.getScheduleCycle())) {
                    throw new ValidException("请检查调度周期格式");
                }
                JobDataMap newJobDataMap = new JobDataMap();
                newJobDataMap.put("groupTaskService", this);
                newJobDataMap.put("webAdminUser", currentWebadminUserEntity);
                newJobDataMap.put("groupTask", entity);
                QuartzManager.addJob(entity.getId(), "GroupTask", entity.getId(), "GroupTask", TaskJob.class, entity.getScheduleCycle(), newJobDataMap);
            }
            groupTaskDao.save(entity);
            operationLogService.insert(new OperationLog("任务组管理—添加", "接入任务管理-任务组管理", "添加了任务组名称为:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务组管理—添加", "接入任务管理-任务组管理", "添加了任务组名称为:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 修改任务组数据
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        GroupTask entity = groupTaskDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            GroupTask model = MyUtils.model2Entity(params, GroupTask.class);
            GroupTask jobDataSource = MyUtils.copyProperties(model, entity);
            tokenService.updateUpdateIdAndTime(request, jobDataSource);
            QuartzManager.removeJob(entity.getId(), "GroupTask", entity.getId(), "GroupTask");

            if ("周期调度".equals(jobDataSource.getOperationMode())) {
                /*调度周期*/
                if (MyUtils.isEmpty(jobDataSource.getScheduleCycle())) {
                    throw new ValidException("调度周期不可为空");
                }
                if (!CronExpression.isValidExpression(jobDataSource.getScheduleCycle())) {
                    throw new ValidException("请检查调度周期格式");
                }
                JobDataMap newJobDataMap = new JobDataMap();
                newJobDataMap.put("groupTaskService", this);
                newJobDataMap.put("webAdminUser", currentWebadminUserEntity);
                newJobDataMap.put("groupTask", entity);
                QuartzManager.addJob(entity.getId(), "GroupTask", entity.getId(), "GroupTask", TaskJob.class, entity.getScheduleCycle(), newJobDataMap);
                jobDataSource.setOperationMode("周期调度");
            } else {
                jobDataSource.setOperationMode("手动模式");
            }
            groupTaskDao.save(jobDataSource);
            operationLogService.insert(new OperationLog("任务组管理—修改", "接入任务管理-任务组管理", "修改了任务组名称为:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务组管理—修改", "接入任务管理-任务组管理", "修改了任务组名称为:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 删除任务组数据
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        GroupTask entity = groupTaskDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        List<Tasks> byGroupTaskId = tasksService.findByGroupTaskId(id);
        if (!byGroupTaskId.isEmpty()) {
            throw new ValidException("该任务组下存在任务，请先清除相关任务！");
        }
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            QuartzManager.removeJob(entity.getId(), "GroupTask", entity.getId(), "GroupTask");
            groupTaskDao.delete(entity);
            operationLogService.insert(new OperationLog("任务组管理—删除", "接入任务管理-任务组管理", "删除任务组" + entity.getName(), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务组管理—删除", "接入任务管理-任务组管理", "删除任务组" + entity.getName(), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 修改任务组状态  审批成功、已下线
     */
    public void updateStatus(JSONObject params, HttpServletRequest request) throws Exception {

        String id = params.getString("id");
        GroupTask entity = groupTaskDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            String id1 = entity.getId();
            QuartzManager.removeJob(id1, "GroupTask", id1, "GroupTask");
            if ("启用".equals(params.getString("status"))) {
                entity.setStatus("已上线");
                if ("周期调度".equals(entity.getOperationMode())) {
                    JobDataMap newJobDataMap = new JobDataMap();
                    newJobDataMap.put("groupTaskService", this);
                    newJobDataMap.put("webAdminUser", currentWebadminUserEntity);
                    newJobDataMap.put("groupTask", entity);
                    QuartzManager.addJob(id1, "GroupTask", id1, "GroupTask", TaskJob.class, entity.getScheduleCycle(), newJobDataMap);
                }
            } else {
                entity.setStatus("已下线");
            }
            tokenService.updateUpdateIdAndTime(request, entity);
            groupTaskDao.save(entity);
            operationLogService.insert(new OperationLog("任务组管理—修改任务组状态", "接入任务管理-任务组管理", "修改了任务组名称为:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务组管理—修改任务组状态", "接入任务管理-任务组管理", "修改了任务组名称为:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    public void execute(String id, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        execute(id, currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
    }

    /**
     * 手动执行
     */
    public void execute(String id, String currentWebadminUserId, String currentWebadminUserName) throws Exception {
        //查询任务组信息
        GroupTask entity = groupTaskDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        //查询任务组下是否有任务
        List<Tasks> tasksList = tasksService.findByGroupTaskId(id);
        if (tasksList.isEmpty()) {
            throw new ValidException("该任务组下无可执行任务！");
        }
        //循环遍历任务
        //产生任务组执行日志
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("groupTaskLogId", id);
        jsonObject.put("groupTaskName", entity.getName());
        jsonObject.put("sourceCatalog", entity.getSourceCatalog());
        jsonObject.put("dataSourceSourceId", entity.getDataSourceSourceId());
        jsonObject.put("dataSourceSourceName", entity.getDataSourceSourceName());
        jsonObject.put("targetCatalog", entity.getTargetCatalog());
        jsonObject.put("dataSourceTargetId", entity.getDataSourceTargetId());
        jsonObject.put("dataSourceTargetName", entity.getDataSourceTargetName());
        if (!MyUtils.isEmpty(id) && "周期调度".equals(entity.getOperationMode()) && "已上线".equals(entity.getStatus())) {
            jsonObject.put("scheduleCycle", entity.getScheduleCycle());
        }

        jsonObject.put("operationMode", entity.getOperationMode());
        GroupTaskLog insert = groupTaskLogService.insert(jsonObject, currentWebadminUserId, currentWebadminUserName);
        for (Tasks task : tasksList) {
            tasksService.execute(task.getId(), currentWebadminUserId, currentWebadminUserName, insert.getId());
        }
    }

    public boolean existsByDataSourceSourceIdOrDataSourceTargetId(String dataSourceSourceId, String dataSourceTargetId) {
        return groupTaskDao.existsByDataSourceSourceIdOrDataSourceTargetId(dataSourceSourceId, dataSourceTargetId);
    }
}
