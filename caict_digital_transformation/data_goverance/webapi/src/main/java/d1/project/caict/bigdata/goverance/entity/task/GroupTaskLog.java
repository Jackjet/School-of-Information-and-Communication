package d1.project.caict.bigdata.goverance.entity.task;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Date;

/**
 * @author maoyy
 */

@Entity
@Table(name = "d1_group_task_log")
@ApiModel(value = "GroupTaskLog", description = "任务组日志表")
public class GroupTaskLog extends BaseCreateAndUpdateEntity {
    @Column(length = 50)
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @Column(length = 100)
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @Column(length = 50)
    @ApiModelProperty("数据来源类型")
    private String sourceCatalog;
    @Column(length = 50)
    @ApiModelProperty("数据来源id")
    private String dataSourceSourceId;
    @Column(length = 50)
    @ApiModelProperty("数据来源名称")
    private String dataSourceSourceName;
    @Column(length = 200)
    @ApiModelProperty("目标源类型")
    private String targetCatalog;
    @Column(length = 50)
    @ApiModelProperty("目标源id")
    private String dataSourceTargetId;
    @Column(length = 50)
    @ApiModelProperty("目标源名称")
    private String dataSourceTargetName;
    @Column(length = 50)
    @ApiModelProperty("调度周期")
    private String scheduleCycle;
    @ApiModelProperty("作业开始时间")
    private Date startTime;
    @ApiModelProperty("作业结束时间")
    private Date endTime;
    @Column(length = 20)
    @ApiModelProperty("状态")
    private String status;
    @Column(length = 150)
    @ApiModelProperty("回调URL")
    private String callBackUrl;
    @ApiModelProperty("运行模式")
    private String operationMode;

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
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

    public String getSourceCatalog() {
        return sourceCatalog;
    }

    public void setSourceCatalog(String sourceCatalog) {
        this.sourceCatalog = sourceCatalog;
    }

    public String getDataSourceSourceId() {
        return dataSourceSourceId;
    }

    public void setDataSourceSourceId(String dataSourceSourceId) {
        this.dataSourceSourceId = dataSourceSourceId;
    }

    public String getDataSourceSourceName() {
        return dataSourceSourceName;
    }

    public void setDataSourceSourceName(String dataSourceSourceName) {
        this.dataSourceSourceName = dataSourceSourceName;
    }

    public String getTargetCatalog() {
        return targetCatalog;
    }

    public void setTargetCatalog(String targetCatalog) {
        this.targetCatalog = targetCatalog;
    }

    public String getDataSourceTargetId() {
        return dataSourceTargetId;
    }

    public void setDataSourceTargetId(String dataSourceTargetId) {
        this.dataSourceTargetId = dataSourceTargetId;
    }

    public String getDataSourceTargetName() {
        return dataSourceTargetName;
    }

    public void setDataSourceTargetName(String dataSourceTargetName) {
        this.dataSourceTargetName = dataSourceTargetName;
    }

    public String getScheduleCycle() {
        return scheduleCycle;
    }

    public void setScheduleCycle(String scheduleCycle) {
        this.scheduleCycle = scheduleCycle;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCallBackUrl() {
        return callBackUrl;
    }

    public void setCallBackUrl(String callBackUrl) {
        this.callBackUrl = callBackUrl;
    }
}
