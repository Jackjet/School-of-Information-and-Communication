package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 统计活跃用户
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_statistic_active_user")
@ApiModel(value = "StatisticActiveUser", description = "统计活跃用户")
public class StatisticActiveUser extends BaseEntity {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("登录时间")
    private String visitDate;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(String visitDate) {
        this.visitDate = visitDate;
    }
}

