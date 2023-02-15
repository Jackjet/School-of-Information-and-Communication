package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AppUserMessage", description = "APP用户通知消息")
@Entity
@Table(name = "d1_app_user_message")
@Data
public class AppUserMessage extends BaseCreateEntity {
    @ApiModelProperty(value = "通知消息ID")
    private String msgId;
    @ApiModelProperty(value = "是否已读（0:未读，1：已读）")
    private String isRead;
}
