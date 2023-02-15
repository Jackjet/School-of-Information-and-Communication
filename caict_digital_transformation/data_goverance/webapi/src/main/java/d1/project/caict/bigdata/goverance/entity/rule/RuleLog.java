package d1.project.caict.bigdata.goverance.entity.rule;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "d1_rule_log")
@ApiModel(value = "RuleLog", description = "规则日志表")
public class RuleLog extends BaseEntity {

    @ApiModelProperty("规则编码")
    private String ruleId;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("任务编码")
    private String taskId;

    @ApiModelProperty("任务名称")
    private String taskName;

    @ApiModelProperty("模板编码")
    private String templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("作业开始时间")
    private Calendar startTime;

    @ApiModelProperty("作业结束时间")
    private Calendar endTime;

    @ApiModelProperty("执行结果")
    private String status;

    @ApiModelProperty("检查条数")
    private Long checkCount;

    @ApiModelProperty("错误条数")
    private Long errorCount;

    @ApiModelProperty("运行sql")
    @Column(columnDefinition = "TEXT")
    private String ruleSql;



    @ApiModelProperty("日志详情")
    @Column(columnDefinition = "TEXT")
    private String logDesc;

    public String getRuleSql() {
        return ruleSql;
    }

    public void setRuleSql(String ruleSql) {
        this.ruleSql = ruleSql;
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

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
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
