package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Calendar;

/**
 * 我的订阅
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_my_subscribe")
@ApiModel(value = "MySubscribe", description = "我的订阅")
public class MySubscribe extends BaseCreateEntity {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("cms内容标识")
    private String cmsContentId;

    @ApiModelProperty("订阅内容类型：0 产业动态、1 行业资讯、2 政策解读、3 活动")
    private String type;

    @ApiModelProperty("订阅内容标题")
    private String title;

    @ApiModelProperty("订阅内容图片")
    private String pic;

    @ApiModelProperty("订阅内容简介")
    private String synopsis;

    @ApiModelProperty("内容时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar time;

    @ApiModelProperty("订阅内容地址")
    private String url;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getCmsContentId() {
        return cmsContentId;
    }

    public void setCmsContentId(String cmsContentId) {
        this.cmsContentId = cmsContentId;
    }
}
