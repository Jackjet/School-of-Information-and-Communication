package d1.project.questionnaire.entity.base;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Date;

/**
 * @author 17503
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public class BaseCreateEntity extends BaseEntity {

    @ApiModelProperty(value = "创建人Id")
    @Column(length = 32)
    private String createById;

    @ApiModelProperty(value = "创建时间", example = "2018-01-01 01:01:01")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    public String getCreateById() {
        return createById;
    }

    public void setCreateById(String createById) {
        this.createById = createById;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
