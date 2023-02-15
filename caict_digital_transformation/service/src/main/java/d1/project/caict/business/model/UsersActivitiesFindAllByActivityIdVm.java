package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class UsersActivitiesFindAllByActivityIdVm extends UserFindAllVm {

    @ApiModelProperty("活动id")
    @NotBlank(message = "活动id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
