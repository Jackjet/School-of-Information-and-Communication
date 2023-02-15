package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
public class MyCollectInsertVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户Id")
    private String userId;

    @NotBlank(message = "cms内容标识不能为空")
    @ApiModelProperty("cms内容标识")
    private String cmsContentId;

    @NotBlank(message = "收藏内容类型不能为空")
    @ApiModelProperty("收藏内容类型：0：服务:1：文章")
    private String type;

    @ApiModelProperty("收藏内容标题")
    private String title;

    @ApiModelProperty("收藏内容简介")
    private String synopsis;

    @ApiModelProperty("收藏内容地址")
    private String url;

    @ApiModelProperty("收藏内容主图")
    private String pic;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
