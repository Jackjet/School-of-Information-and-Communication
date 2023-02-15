package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 消息内容
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_message_content")
@ApiModel(value = "MessagesContent", description = "消息内容")
public class MessageContent extends BaseCreateEntity {
    @ApiModelProperty("发送方用户Id")
    private String senderId;

    @ApiModelProperty("消息类型，支持：0 私有，1 全局")
    @Column(length = 1)
    private String type;

    @ApiModelProperty("消息标题")
    private String title;

    @ApiModelProperty("消息内容")
    @Column(columnDefinition = "TEXT")
    private String text;

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

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
