package d1.project.questionnaire.model.exam.examinationquestion;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 16:38
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminationQuestionInsert extends ExaminationQuestionInsertVm {

    @ApiModelProperty(value = "考试id")
    private String examinationId;
}
