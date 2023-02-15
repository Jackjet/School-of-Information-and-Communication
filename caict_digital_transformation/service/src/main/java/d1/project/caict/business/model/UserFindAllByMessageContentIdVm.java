package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
@ApiModel(value = "UserFindAllByMessageContentIdVm", description = "查询该条消息的接收者")
public class UserFindAllByMessageContentIdVm extends UserFindAllVm {
    @ApiModelProperty("消息内容id")
    @NotBlank(message = "消息内容id不能为空")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

