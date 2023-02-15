package d1.project.caict.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kikki
 */
@Entity
@Table(name = "d1_system_settings")
@ApiModel(value = "SystemSettings", description = "系统设置")
public class SystemSettings extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("类型")
    @Column(length = 2)
    private String type;

    @ApiModelProperty("值")
    @Column(columnDefinition = "TEXT")
    private String value;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
