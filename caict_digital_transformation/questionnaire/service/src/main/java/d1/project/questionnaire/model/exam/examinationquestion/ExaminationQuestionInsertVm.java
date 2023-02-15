package d1.project.questionnaire.model.exam.examinationquestion;

import lombok.Data;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 16:38
 */
@Data
public class ExaminationQuestionInsertVm {

    private String questionId;

    private Integer number;

    private Integer score;

    private String type;
}
