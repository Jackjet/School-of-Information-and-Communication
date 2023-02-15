package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "d1_invite_code")
@ApiModel(value = "InviteCode", description = "邀请码管理")
@Data
public class InviteCode extends BaseCreateEntity {

    @ApiModelProperty(value = "被邀请者id")
    private String inviteeId;

    @Column(length = 6)
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "分享id")
    private String shareRecordId;

    @Column(length = 1)
    @ApiModelProperty(value = "0:未使用,1:使用")
    private String life;

    @Column(length = 1)
    @ApiModelProperty(value = "是上锁 0:未上锁,1:已上锁")
    private boolean isLock;
}
