package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 我的收藏
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_my_collect")
@ApiModel(value = "MyCollect", description = "我的收藏")
public class MyCollect extends BaseCreateEntity {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("cms内容标识")
    private String cmsContentId;

    @ApiModelProperty("收藏内容类型：0：服务:1：文章")
    @Column(length = 1)
    private String type;

    @ApiModelProperty("收藏内容标题")
    private String title;

    @ApiModelProperty("收藏内容主图")
    private String pic;

    @ApiModelProperty("收藏内容简介")
    private String synopsis;

    @ApiModelProperty("收藏内容地址")
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
