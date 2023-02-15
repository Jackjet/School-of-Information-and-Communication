package d1.project.cms.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * @author Buter
 * @date 2020/3/20 14:40
 */
@Entity
@Table(name = "d1_sample_entity")
@ApiModel(value = "SampleEntity", description = "Entity示例")
public class SampleEntity extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("数字类型Field")
    private Integer iField;

    @ApiModelProperty("字符串类型Field")
    private String sField;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "时间类型Field", example = "2018-01-23 09:12:32")
    private Calendar tField;

    public Integer getiField() {
        return iField;
    }

    public void setiField(Integer iField) {
        this.iField = iField;
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public Calendar gettField() {
        return tField;
    }

    public void settField(Calendar tField) {
        this.tField = tField;
    }
}
