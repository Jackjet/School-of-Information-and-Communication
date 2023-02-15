package d1.project.caict.bigdata.goverance.entity.task;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

/**
 * @author maoyy
 */

@Entity
@Table(name = "d1_task_log")
@ApiModel(value = "TaskLog", description = "任务日志表")
public class TaskLog extends BaseEntity {
    @Column(length = 50)
    @ApiModelProperty("任务组日志id")
    private String groupTaskLogId;
    @Column(length = 50)
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @Column(length = 50)
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @Column(length = 50)
    @ApiModelProperty("任务id")
    private String taskId;
    @Column(length = 50)
    @ApiModelProperty("任务名称")
    private String taskName;
    @Column(length = 30)
    @ApiModelProperty("源表")
    private String sourceTable;
    @Column(length = 30)
    @ApiModelProperty("目标表")
    private String targetTable;
    @Column(length = 10)
    @ApiModelProperty("增全量策略,增量、全量")
    private String strategy;
    @Column(length = 10)
    @ApiModelProperty("状态,执行成功、执行失败")
    private String status;
    @ApiModelProperty("作业开始时间")
    private Calendar startTime;
    @ApiModelProperty("作业结束时间")
    private Calendar endTime;
    @ApiModelProperty("作业执行时长")
    private Long duration;
    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("日志详情")
    private String logDesc;
    @Column(length = 150)
    @ApiModelProperty("回调URL")
    private String callBackUrl;

    public String getGroupTaskLogId() {
        return groupTaskLogId;
    }

    public void setGroupTaskLogId(String groupTaskLogId) {
        this.groupTaskLogId = groupTaskLogId;
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

    public String getTaskId() {
        return taskId;
    }

    public void setTaskId(String taskId) {
        this.taskId = taskId;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public Long getDuration() {
        return duration;
    }

    public void setDuration(Long duration) {
        this.duration = duration;
    }

    public String getLogDesc() {
        return logDesc;
    }

    public void setLogDesc(String logDesc) {
        this.logDesc = logDesc;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }
}
