package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import java.util.Calendar;

/**
 * @author maorui
 */
public class MySubscribeInsertVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户Id")
    private String userId;

    @NotBlank(message = "cms内容标识不能为空")
    @ApiModelProperty("cms内容标识")
    private String cmsContentId;

    @NotBlank(message = "订阅内容类型不能为空")
    @ApiModelProperty("订阅内容类型：0 产业动态、1 行业资讯、2 政策解读、3 活动")
    private String type;

    @NotBlank(message = "订阅内容标题不能为空")
    @ApiModelProperty("订阅内容标题")
    private String title;

    @ApiModelProperty("订阅内容图片")
    private String pic;

    @ApiModelProperty("订阅内容简介")
    private String synopsis;

    @ApiModelProperty("内容时间")
    private Calendar time;

    @ApiModelProperty("订阅内容地址")
    private String url;

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public Calendar getTime() {
        return time;
    }

    public void setTime(Calendar time) {
        this.time = time;
    }

    public String getCmsContentId() {
        return cmsContentId;
    }

    public void setCmsContentId(String cmsContentId) {
        this.cmsContentId = cmsContentId;
    }
}
