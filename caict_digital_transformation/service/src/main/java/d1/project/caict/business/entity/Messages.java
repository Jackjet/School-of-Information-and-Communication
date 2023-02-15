package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 我的消息
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_messages")
@ApiModel(value = "Messages", description = "消息内容")
public class Messages extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("消息内容id")
    private String messageContentId;

    @ApiModelProperty("接收者id")
    private String receiverId;

    @ApiModelProperty("状态:0未读，1已读，2删除")
    @Column(length = 1)
    private String status;

    public String getMessageContentId() {
        return messageContentId;
    }

    public void setMessageContentId(String messageContentId) {
        this.messageContentId = messageContentId;
    }

    public String getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(String receiverId) {
        this.receiverId = receiverId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
