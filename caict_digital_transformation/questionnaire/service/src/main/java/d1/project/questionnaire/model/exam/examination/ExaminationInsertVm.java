package d1.project.questionnaire.model.exam.examination;

import d1.project.questionnaire.model.exam.examinationquestion.ExaminationQuestionInsertVm;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminationInsertVm extends ExaminationInsert {
    private List<ExaminationQuestionInsertVm> question;
}
