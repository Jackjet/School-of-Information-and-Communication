package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.model.PageableVm;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
@ApiModel(value = "MessageContentFindAllByUserIdVm", description = "查询我的消息")
public class MessageContentFindAllByUserIdVm extends PageableVm {
    @ApiModelProperty("状态:0未读，1已读，2删除")
    @NotBlank(message = "消息状态不能为空")
    private String status;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

