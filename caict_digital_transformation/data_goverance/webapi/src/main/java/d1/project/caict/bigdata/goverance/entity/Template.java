package d1.project.caict.bigdata.goverance.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_template")
@ApiModel(value = "Template", description = "模板表")
public class Template extends BaseCreateAndUpdateEntity {

    /**唯一性*/
    @ApiModelProperty("模板名称")
    private String name;

    /**完整性非空约束、实体唯一性约束、有效性长度约束 、
    有效性标志取值约束、有效性代码值域约束、有效性内容规范约束、
    存在一致性依赖约束、等值一致性依赖约束 、逻辑一致性依赖约束、
    取值准确性约束、时效性约束*/
    @ApiModelProperty("约束分类")
    private String constraintType;

    @ApiModelProperty("数据问题分类")
    private String dataQualityType;

    /**技术、业务*/
    @ApiModelProperty("业务技术分类")
    private String businessTechnicalType;

    /**单行、多行*/
    @ApiModelProperty("结果集")
    private String result;

    /**根据不同的约束分类，带出示例SQL语句*/
    @ApiModelProperty("检查SQL")
    private String sql;

    /**json格式，[
    {
        "变量名":"",
        "变量含义":""
    }
    ]*/
    @ApiModelProperty("变量")
    @Column(columnDefinition = "TEXT")
    private String params;
    /**规则的编辑、删除不影响此值*/
    @ApiModelProperty("引用次数")
    private Integer referencesCount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("修改人名称")
    private String updateByName;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getDataQualityType() {
        return dataQualityType;
    }

    public void setDataQualityType(String dataQualityType) {
        this.dataQualityType = dataQualityType;
    }

    public String getBusinessTechnicalType() {
        return businessTechnicalType;
    }

    public void setBusinessTechnicalType(String businessTechnicalType) {
        this.businessTechnicalType = businessTechnicalType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getReferencesCount() {
        return referencesCount;
    }

    public void setReferencesCount(Integer referencesCount) {
        this.referencesCount = referencesCount;
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
