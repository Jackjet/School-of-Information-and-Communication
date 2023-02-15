package d1.project.caict.bigdata.goverance.entity.rule;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_rule")
@ApiModel(value = "Rule", description = "规则表")
public class Rule extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("规则名称:唯一性")
    private String name;

    @ApiModelProperty("技术负责人")
    private String technicalLeader;

    @ApiModelProperty("业务负责人")
    private String businessLeader;

    @ApiModelProperty("变更说明")
    private String updateMessage;

    @ApiModelProperty("模板编码:模板表的主键id")
    private String templateId;

    @ApiModelProperty("模板名称")
    private String templateName;

    @ApiModelProperty("变量:json格式，[\n" +
            "    {\n" +
            "        \"变量名\":\"\",\n" +
            "        \"变量含义\":\"\",\n" +
            "        \"值\":\"\"\n" +
            "    }\n" +
            "]")
    @Column(columnDefinition = "TEXT")
    private String params;

    @ApiModelProperty("关联工单")
    private Long workOrderCount;

    @ApiModelProperty("备注")
    private String remark;

   @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("修改人名称")
    private String updateByName;

    @ApiModelProperty("检查SQL")
    @Column(columnDefinition = "TEXT")
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalLeader() {
        return technicalLeader;
    }

    public void setTechnicalLeader(String technicalLeader) {
        this.technicalLeader = technicalLeader;
    }

    public String getBusinessLeader() {
        return businessLeader;
    }

    public void setBusinessLeader(String businessLeader) {
        this.businessLeader = businessLeader;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
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

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Long getWorkOrderCount() {
        return workOrderCount;
    }

    public void setWorkOrderCount(Long workOrderCount) {
        this.workOrderCount = workOrderCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }
}
