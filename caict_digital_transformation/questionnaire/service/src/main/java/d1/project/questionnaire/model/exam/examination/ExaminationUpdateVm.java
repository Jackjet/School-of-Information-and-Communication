package d1.project.questionnaire.model.exam.examination;

import d1.project.questionnaire.model.exam.examinationquestion.ExaminationQuestionInsertVm;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class ExaminationUpdateVm {
    private String id;
    private String name;
    private Integer totalSorce;
    private List<ExaminationQuestionInsertVm> question;
}
