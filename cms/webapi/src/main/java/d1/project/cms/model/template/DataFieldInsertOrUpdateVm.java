package d1.project.cms.model.template;

import io.swagger.annotations.ApiModelProperty;

/**
 * @author baozh
 */
public class DataFieldInsertOrUpdateVm {
    @ApiModelProperty("ID")
    private String id;

    @ApiModelProperty("模板数据类型 0列表数据模型，1.文档数据模型")
    private String type;

    @ApiModelProperty("模板ID")
    private String templateId;

    @ApiModelProperty("字段")
    private String field;

    @ApiModelProperty("字段名称")
    private String fieldName;

    @ApiModelProperty("数据类型")
    private String dataType;

    @ApiModelProperty("是否允许为空（NO/YES）")
    private String isNull;

    @ApiModelProperty("默认值")
    private String defaultValue;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getFieldName() {
        return fieldName;
    }

    public void setFieldName(String fieldName) {
        this.fieldName = fieldName;
    }

    public String getDataType() {
        return dataType;
    }

    public void setDataType(String dataType) {
        this.dataType = dataType;
    }

    public String getIsNull() {
        return isNull;
    }

    public void setIsNull(String isNull) {
        this.isNull = isNull;
    }

    public String getDefaultValue() {
        return defaultValue;
    }

    public void setDefaultValue(String defaultValue) {
        this.defaultValue = defaultValue;
    }
}
