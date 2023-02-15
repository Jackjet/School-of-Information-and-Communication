package d1.project.caict.bigdata.goverance.controller.webadmin.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.task.TaskLog;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.task.TaskLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/taskLog")
@Api(value = "/webadmin/taskLog", tags = "任务日志管理")
public class TaskLogController {
    private final TaskLogService taskLogService;

    @Autowired
    public TaskLogController(TaskLogService taskLogService){
        this.taskLogService=taskLogService;
    }

    @ApiOperation(value = "查询任务日志信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<TaskLog>> findAll(TaskLogGetVm model) {
        try {
            Page<TaskLog> all = taskLogService.findAll((JSONObject) JSON.toJSON(model));
            return ResultUtil.success(Constants.SUCCESS, all);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<TaskLog> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            TaskLog taskLog = taskLogService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, taskLog);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class TaskLogGetVm extends PageableVm {
    @ApiModelProperty("任务名称")
    private String taskName;
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @ApiModelProperty("状态,执行成功、执行失败")
    private String status;
    @ApiModelProperty("执行开始时间")
    private String startTimeThan;
    @ApiModelProperty("执行结束时间")
    private String startTimeLess;
    @ApiModelProperty("任务组日志id")
    private String groupTaskLogId;

    public String getGroupTaskLogId() {
        return groupTaskLogId;
    }

    public void setGroupTaskLogId(String groupTaskLogId) {
        this.groupTaskLogId = groupTaskLogId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getGroupTaskId() {
        return groupTaskId;
    }

    public void setGroupTaskId(String groupTaskId) {
        this.groupTaskId = groupTaskId;
    }

    public String getGroupTaskName() {
        return groupTaskName;
    }

    public void setGroupTaskName(String groupTaskName) {
        this.groupTaskName = groupTaskName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStartTimeThan() {
        return startTimeThan;
    }

    public void setStartTimeThan(String startTimeThan) {
        this.startTimeThan = startTimeThan;
    }

    public String getStartTimeLess() {
        return startTimeLess;
    }

    public void setStartTimeLess(String startTimeLess) {
        this.startTimeLess = startTimeLess;
    }
}