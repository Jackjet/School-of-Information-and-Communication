package d1.project.questionnaire.model.exam.question;

import d1.project.questionnaire.entity.examination.Option;
import d1.project.questionnaire.entity.examination.Question;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class QuestionFindReturnVm {
    @ApiModelProperty(value = "试题")
    private Question question;
    @ApiModelProperty(value = "选项")
    List<Option> optionList;
}
