package d1.project.questionnaire.model.exam.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class AppExamReturnVm {
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "考试名")
    private String name;
    @ApiModelProperty(value = "考题数量")
    private Long questionNum = 0L;
    @ApiModelProperty(value = "总分")
    private Long totalScore = 0L;
    @ApiModelProperty(value = "试题")
    private List<AppExamQuestionReturnVm> question;
}
