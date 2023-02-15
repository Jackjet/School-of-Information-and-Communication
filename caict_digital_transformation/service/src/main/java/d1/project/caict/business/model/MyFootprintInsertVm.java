package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
public class MyFootprintInsertVm {
    @NotBlank(message = "用户id不能为空")
    @ApiModelProperty("用户Id")
    private String userId;

    @NotBlank(message = "访问内容标题不能为空")
    @ApiModelProperty("访问内容标题")
    private String title;

    @ApiModelProperty("访问内容地址")
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
}
