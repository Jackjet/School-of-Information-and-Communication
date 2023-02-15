package d1.project.caict.bigdata.goverance.controller.webadmin.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.service.task.TasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("noauth")
@RestController
@RequestMapping("/webadmin/service/taskCallBack")
@Api(value = "/webadmin/service/taskCallBack", tags = "回调任务")
public class TaskCallBackController {

    private final TasksService tasksService;

    public TaskCallBackController(TasksService tasksService) {
        this.tasksService = tasksService;
    }

    @ApiOperation(value = "更新任务日志、任务组日志、规则日志信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody TaskCallBackVm model, HttpServletRequest request) throws Exception {
        try {
            tasksService.taskCallBack((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class TaskCallBackVm {
    @ApiModelProperty("任务组日志id")
    private String groupTaskLogId;

    @NotBlank(message = "任务日志id不能为空")
    @ApiModelProperty("任务日志id")
    private String taskLogId;
    @NotBlank(message = "作业开始时间不能为空")
    @ApiModelProperty("作业开始时间")
    private String startTime;
    @NotBlank(message = "作业结束时间不能为空")
    @ApiModelProperty("作业结束时间")
    private String endTime;
    @NotBlank(message = "状态不能为空")
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("检查条数")
    private Long checkCount;
    @ApiModelProperty("错误条数")
    private Long errorCount;
    @ApiModelProperty("日志详情")
    private String logDesc;

    public String getGroupTaskLogId() {
        return groupTaskLogId;
    }

    public void setGroupTaskLogId(String groupTaskLogId) {
        this.groupTaskLogId = groupTaskLogId;
    }

    public String getTaskLogId() {
        return taskLogId;
    }

    public void setTaskLogId(String taskLogId) {
        this.taskLogId = taskLogId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Long getCheckCount() {
        return checkCount;
    }

    public void setCheckCount(Long checkCount) {
        this.checkCount = checkCount;
    }

    public Long getErrorCount() {
        return errorCount;
    }

    public void setErrorCount(Long errorCount) {
        this.errorCount = errorCount;
    }

    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
    }
}
