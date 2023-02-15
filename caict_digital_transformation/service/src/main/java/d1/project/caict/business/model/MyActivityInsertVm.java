package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
public class MyActivityInsertVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户id")
    private String userId;

    @NotBlank(message = "活动id不能为空")
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

    public String getActivityId() {
        return activityId;
    }

    public void setActivityId(String activityId) {
        this.activityId = activityId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
