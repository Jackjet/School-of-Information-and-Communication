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
@Table(name = "d1_tasks")
@ApiModel(value = "Tasks", description = "任务表")
public class Tasks extends BaseCreateAndUpdateEntity {
    @Column(length = 50)
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @Column(length = 50)
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @ApiModelProperty("规则id")
    private String ruleId;
    @ApiModelProperty("规则名称")
    private String ruleName;
    @Column(length = 50)
    @ApiModelProperty("名称")
    private String name;
    @Column(length = 50)
    @ApiModelProperty("数据来源id")
    private String dataSourceSourceId;
    @Column(length = 50)
    @ApiModelProperty("数据来源名称")
    private String dataSourceSourceName;
    @Column(length = 50)
    @ApiModelProperty("目标源id")
    private String dataSourceTargetId;
    @Column(length = 50)
    @ApiModelProperty("目标源名称")
    private String dataSourceTargetName;
    @Column(length = 30)
    @ApiModelProperty("源表")
    private String sourceTable;
    @Column(length = 30)
    @ApiModelProperty("目标表")
    private String targetTable;
    @Column(length = 10)
    @ApiModelProperty("增全量策略,增量、全量")
    private String strategy;
    @Column(length = 200)
    @ApiModelProperty("执行API")
    private String api;
    @Column(length = 10)
    @ApiModelProperty("请求方式")
    private String method;
    @Column(length = 256)
    @ApiModelProperty("header")
    private String header;
    @Column(length = 256)
    @ApiModelProperty("URL参数")
    private String urlParams;
    @Column(length = 512)
    @ApiModelProperty("Body")
    private String body;
    @Column(length = 10)
    @ApiModelProperty("状态")
    private String status;
    @Column(length = 512)
    @ApiModelProperty("备注")
    private String remark;

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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
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

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
