package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 统计新增新闻
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_statistic_news")
@ApiModel(value = "StatisticNews", description = "统计新增新闻")
public class StatisticNews extends BaseCreateEntity {
    @ApiModelProperty("新增数量")
    private int count;

    @ApiModelProperty("新闻类型")
    private String type;

    @ApiModelProperty("新增日期 yyyy-MM-dd ")
    private String day;

    @ApiModelProperty("新增时间 HH:mm:ss")
    private String time;

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
