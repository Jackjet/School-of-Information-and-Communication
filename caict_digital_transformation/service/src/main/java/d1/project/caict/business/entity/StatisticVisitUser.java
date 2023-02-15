package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * 统计系统访问人数
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_statistic_visit_user")
@ApiModel(value = "StatisticVisitUser", description = "统计系统访问人数")
public class StatisticVisitUser extends BaseEntity {

    @ApiModelProperty("访问标识")
    private String flag;

    @ApiModelProperty("访问时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar visitDate;

    @ApiModelProperty("访问日期 yyyy-MM-dd ")
    private String day;

    @ApiModelProperty("访问时间 HH:mm:ss")
    private String time;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public Calendar getVisitDate() {
        return visitDate;
    }

    public void setVisitDate(Calendar visitDate) {
        this.visitDate = visitDate;
    }

    public String getDay() {
        return day;
    }

    public void setDay(String day) {
        this.day = day;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
