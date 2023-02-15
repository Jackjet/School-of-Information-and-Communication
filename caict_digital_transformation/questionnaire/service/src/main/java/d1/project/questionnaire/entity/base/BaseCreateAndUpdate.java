package d1.project.questionnaire.entity.base;

import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Kikki 2019/8/26 10:09
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreateAndUpdate extends BaseCreateEntity {
    @ApiModelProperty(value = "最后修改人Id")
    private String updateById;

    @ApiModelProperty(value = "最后修改时间", example = "2018-01-01 01:01:01")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;


    public String getUpdateById() {
        return updateById;
    }

    public void setUpdateById(String updateById) {
        this.updateById = updateById;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
