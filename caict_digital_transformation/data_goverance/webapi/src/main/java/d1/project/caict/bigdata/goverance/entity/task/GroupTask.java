package d1.project.caict.bigdata.goverance.entity.task;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author maoyy
 */

@Entity
@Table(name = "d1_group_task")
@ApiModel(value = "GroupTask", description = "任务组表")
public class GroupTask  extends BaseCreateAndUpdateEntity {
    @Column(length = 50)
    @ApiModelProperty("名称")
    private String name;
    @Column(length = 20)
    @ApiModelProperty("运行模式,手动模式、周期调度")
    private String operationMode;
    @Column(length = 50)
    @ApiModelProperty("调度周期")
    private String scheduleCycle;
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
    @Column(length = 20)
    @ApiModelProperty("状态")
    private String status;
    @Column(length = 512)
    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getScheduleCycle() {
        return scheduleCycle;
    }

    public void setScheduleCycle(String scheduleCycle) {
        this.scheduleCycle = scheduleCycle;
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

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}
