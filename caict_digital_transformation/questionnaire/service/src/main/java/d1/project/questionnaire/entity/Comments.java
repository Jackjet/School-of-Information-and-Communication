package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Comments", description = "评论")
@Entity
@Table(name = "d1_comments")
@Data
public class Comments extends BaseCreateEntity {

    @ApiModelProperty(value = "类型（1:每日一读）")
    private String type;
    @ApiModelProperty(value = "内容id")
    private String contentId;
    @ApiModelProperty(value = "评论内容")
    private String comment;
    @ApiModelProperty(value = "下架 0：下架，1：上架 （后台管理员用）)")
    private String obtained;
}
