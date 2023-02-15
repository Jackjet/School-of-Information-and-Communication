package d1.project.questionnaire.entity.examination;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:53
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExaminationQuestion", description = "考试试题关联")
@Entity
@Table(name = "d1_examination_question")
@Data
public class ExaminationQuestion extends BaseCreateEntity {
    @ApiModelProperty(value = "考试id")
    private String examinationId;
    @ApiModelProperty(value = "试题id")
    private String questionId;
    @ApiModelProperty(value = "类型0单选1多选")
    private String type;
    @ApiModelProperty(value = "序号")
    private Integer number;
    @ApiModelProperty(value = "分值")
    private Integer score;


}
