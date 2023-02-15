package d1.project.caict.bigdata.goverance.service.task;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.task.TaskLogDao;
import d1.project.caict.bigdata.goverance.entity.task.TaskLog;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author maoyy
 */

@Service
public class TaskLogService {
    private final TaskLogDao taskLogDao;
    private final OperationLogService operationLogService;
    final
    WebAdminService webAdminService;

    public TaskLogService(TaskLogDao taskLogDao, OperationLogService operationLogService, WebAdminService webAdminService) {
        this.taskLogDao = taskLogDao;
        this.operationLogService = operationLogService;
        this.webAdminService = webAdminService;
    }

    /**
     * 查询任务日志信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<TaskLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<TaskLog> builder = new SpecificationBuilder<>();
        Specification<TaskLog> specification = builder.init(params)
                .sEqual("status", "status")
                .sEqual("groupTaskId", "groupTaskId")
                .sEqual("groupTaskLogId", "groupTaskLogId")
                .sEqual("groupTaskName", "groupTaskName")
                .sContain("taskName", "taskName")
                .tThanEqual("startTime", "startTimeThan", "yyyy-MM-dd HH:mm:ss")
                .tLessEqual("startTime", "startTimeLess", "yyyy-MM-dd HH:mm:ss")
                .dOrder("startTime").build();
        return this.taskLogDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询任务详细信息
     *
     * @param id
     * @return
     */
    public Optional<TaskLog> findById(String id) {
        return taskLogDao.findById(id);
    }

    /**
     * 添加任务日志信息
     *
     * @param params
     * @throws Exception
     */
    public void insert(JSONObject params, String currentWebadminUserId, String currentWebadminUserName) throws Exception {
//        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            TaskLog taskLog = new TaskLog();
            taskLog.setId(params.getString("id"));
            taskLog.setGroupTaskLogId(params.getString("groupTaskLogId"));
            taskLog.setGroupTaskId(params.getString("groupTaskId"));
            taskLog.setGroupTaskName(params.getString("groupTaskName"));
            taskLog.setTaskId(params.getString("taskId"));
            taskLog.setTaskName(params.getString("taskName"));
            taskLog.setSourceTable(params.getString("sourceTable"));
            taskLog.setTargetTable(params.getString("targetTable"));
            taskLog.setStrategy(params.getString("strategy"));
            Object groupTaskLogId = params.get("groupTaskLogId");
            taskLog.setGroupTaskLogId(groupTaskLogId == null ? null : groupTaskLogId.toString());
            this.taskLogDao.save(taskLog);

            operationLogService.insert(new OperationLog("任务管理—任务手动执行", "接入任务管理-任务管理", "手动执行任务:" + params.getString("name"), "1"), currentWebadminUserId, currentWebadminUserName);

        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务管理—任务手动执行", "接入任务管理-任务管理", "手动执行任务:" + params.getString("name"), "0"), currentWebadminUserId, currentWebadminUserName);
            throw new ValidException(e.getMessage());
        }

    }

    public void update(JSONObject params) throws Exception {
        String id = params.getString("taskLogId");
        TaskLog taskLog = this.taskLogDao.findById(id).orElseThrow(() -> new ValidException("找不到任务日志记录:" + id));
        Date startTime = params.getDate("startTime");
        Date endTime = params.getDate("endTime");
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(startTime);
        calendar2.setTime(endTime);

        taskLog.setStartTime(calendar1);
        taskLog.setEndTime(calendar2);
        long second = getDay(startTime, endTime);
        taskLog.setDuration(second);
        taskLog.setStatus(params.getString("status"));
        taskLog.setLogDesc(params.getString("logDesc"));
        taskLogDao.save(taskLog);
    }

    /**
     * 获取任务日志执行间隔，单位：天
     *
     * @param startTime
     * @param endTime
     * @return
     * @throws Exception
     */
    public Long getDay(Date startTime, Date endTime) {
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        long diff = endTime.getTime() - startTime.getTime();
        Long day = diff / (1000 * 60 * 60 * 24);
        return day;
    }

    public Long countByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Date createTime, Date createTime2) {
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(createTime);
        calendar2.setTime(createTime2);
        return taskLogDao.countByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(calendar1, calendar2);
    }

    public Map<String, Object> countGroupMonthByStartTime(Date createTime, Date createTime2) throws Exception {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Map<String, Object> monthBetween = MyUtils.getMonthBetween(simpleDateFormat.format(createTime), simpleDateFormat.format(createTime2));
        Calendar calendar1 = Calendar.getInstance();
        Calendar calendar2 = Calendar.getInstance();
        calendar1.setTime(createTime);
        calendar2.setTime(createTime2);

        List<Map<String, Long>> maps = taskLogDao.countGroupMonthByStartTime(calendar1, calendar2);
        maps.forEach(stringObjectMap -> {
            Object name = stringObjectMap.get("name");
            Long value = stringObjectMap.get("value");
            monthBetween.replace(String.valueOf(name), value);
        });

        List<String> strings = new ArrayList<>();
        List<Object> longs = new ArrayList<>();
        monthBetween.forEach((s, o) -> {
            strings.add(s);
            longs.add(o);
        });
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("month", strings);
        objectMap.put("count", longs);

        return objectMap;
    }

    long countByGroupTaskLogId(String groupTaskLogId) {
        return taskLogDao.countByGroupTaskLogId(groupTaskLogId);
    }

    long countByGroupTaskLogIdAndStatusIn(String groupTaskLogId, String... status) {
        return taskLogDao.countByGroupTaskLogIdAndStatusIn(groupTaskLogId, Arrays.asList(status));
    }
}
