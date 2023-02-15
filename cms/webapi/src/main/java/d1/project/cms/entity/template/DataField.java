package d1.project.cms.entity.template;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 模板
 * @author baozh
 */
@Entity
@Table(name = "d1_data_field")
@ApiModel(value = "DataField", description = "数据字段")
public class DataField extends BaseCreateAndUpdateEntity {
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
