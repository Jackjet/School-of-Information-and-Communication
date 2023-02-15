package d1.project.questionnaire.model.exam.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Lob;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppExamQuestionReturnVm extends ExamQuestionReturnVm{
    @ApiModelProperty(value = "问题解析")
    @Lob
    private String problemAnalysis;
}
