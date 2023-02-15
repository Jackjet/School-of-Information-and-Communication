package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 我的足迹
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_my_footprint")
@ApiModel(value = "MyFootprint", description = "我的足迹")
public class MyFootprint extends BaseCreateEntity {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("访问内容标题")
    private String title;

    @ApiModelProperty("访问内容地址")
    private String url;

    @ApiModelProperty("访问日期")
    private String day;

    @ApiModelProperty("访问时间")
    private String time;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
