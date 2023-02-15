package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Feedback", description = "用户反馈")
@Entity
@Table(name = "d1_feedback")
@Data
public class Feedback extends BaseCreateEntity {

    @ApiModelProperty(value = "反馈人")
    private String name;

    @ApiModelProperty(value = "电话")
    private String phone;

    @ApiModelProperty(value = "反馈标题")
    @Lob
    private String title;

    @Column(columnDefinition = "longtext")
    @ApiModelProperty(value = "多图片")
    private String images;

    @Column(columnDefinition = "longtext")
    @ApiModelProperty(value = "反馈内容")
    private String detail;

    @ApiModelProperty(value = "qq号")
    private String qqNo;

    @ApiModelProperty(value = "微信号")
    private String weChat;
}
