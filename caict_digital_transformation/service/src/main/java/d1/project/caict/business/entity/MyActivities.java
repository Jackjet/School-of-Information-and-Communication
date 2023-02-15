package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 中间表：用户参与的活动
 *
 * @author: maorui
 * @date: 2020/9/28
 */
@Entity
@Table(name = "d1_my_activities")
@ApiModel(value = "MyActivities", description = "用户参与的活动")
public class MyActivities extends BaseCreateEntity {

    @ApiModelProperty("用户id")
    private String userId;

    @ApiModelProperty("企业名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("联系人姓名")
    private String linkmanName;

    @ApiModelProperty("联系人电话")
    @Column(length = 30)
    private String linkmanPhone;

    @ApiModelProperty("所属行业，多个用“、”分割")
    private String industries;

    @ApiModelProperty("活动id")
    private String activityId;

    @ApiModelProperty("活动网址")
    private String url;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
