package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;


/***
 * 通知表
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Message", description = "通知表")
@Entity
@Table(name = "d1_message")
@Data
public class Message extends BaseCreateAndUpdate {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "详情")
    @Column(columnDefinition = "longtext")
    private String detail;

    @ApiModelProperty(value = "摘要")
    @Column(columnDefinition = "longtext")
    private String summary;

    @ApiModelProperty(value = "外部连接")
    @Column(columnDefinition = "longtext")
    private String externalLinks;

    @ApiModelProperty(value = "消息类型（0:系统 1:回复）")
    private String type;

    @ApiModelProperty(value = "是否推送（0：否，1：是）")
    private String isPush = "0";

}
