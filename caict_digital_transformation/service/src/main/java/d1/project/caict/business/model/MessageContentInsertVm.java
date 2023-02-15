package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.Collection;

/**
 * @author maorui
 */
@ApiModel(value = "MessageContentInsertVm", description = "发送消息")
public class MessageContentInsertVm {
    @ApiModelProperty("发送者id")
    @NotBlank(message = "发送者id不能为空")
    private String senderId;

    @ApiModelProperty("标题")
    @NotBlank(message = "消息标题不能为空")
    private String title;

    @ApiModelProperty("消息内容")
    @NotBlank(message = "消息内容不能为空")
    private String text;

    @Size(min = 1, message = "用户id列表不能为空")
    private Collection<String> userIds;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public Collection<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(Collection<String> userIds) {
        this.userIds = userIds;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }
}

