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
@ApiModel(value = "ThumbUp", description = "点赞表")
@Entity
@Table(name = "d1_thumb_up")
@Data
public class ThumbUp extends BaseCreateEntity {
    @ApiModelProperty(value = "类型（0.笔记,1.评论）")
    @Column(length = 2)
    private String type;

    @ApiModelProperty(value = "内容ID")
    @Column(length = 50)
    private String contentId;
}
