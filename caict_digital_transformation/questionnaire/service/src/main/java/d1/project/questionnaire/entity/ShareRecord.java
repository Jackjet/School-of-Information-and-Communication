package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/***
 * 分享解锁记录表
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ShareRecord", description = "分享解锁记录表")
@Entity
@Table(name = "d1_share_record")
@Data
public class ShareRecord extends BaseCreateEntity {

    @ApiModelProperty(value = "卦象id")
    @Column(length = 20)
    private String hexagramId;

    @ApiModelProperty(value = "卦象名称")
    @Column(length = 20)
    private String hexagramName;

    @ApiModelProperty(value = "邀请码")
    @Transient
    private String inviteCode;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "解锁时间")
    private Date unlockTime;

    @ApiModelProperty(value = "解锁", allowableValues = "0:未解锁,1:已解锁")
    @Column(length = 1)
    private String unfasten;

}
