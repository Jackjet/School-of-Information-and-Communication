package d1.project.questionnaire.entity.examination;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:45
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Question", description = "试题")
@Entity
@Table(name = "d1_question")
@Data
public class Question extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "类型(0单选1多选)")
    private String type;
    @ApiModelProperty(value = "题目")
    @Lob
    private String topic;
    @ApiModelProperty(value = "问题解析")
    @Lob
    private String problemAnalysis;
    @ApiModelProperty(value = "考试分组id")
    private String examinationGroupId;
}
