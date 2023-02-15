package d1.project.questionnaire.model.exam.examination;

import java.lang.String;
import java.util.List;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
public class AppExaminationHandInVm {
    @ApiModelProperty(value = "考试id")
    private String examinationId;
    @ApiModelProperty(value = "营期id")
    private String campPeriodId;
    @ApiModelProperty(value = "学习计划id")
    private String learnPlanId;
    @ApiModelProperty(value = "试题id集")
    private List<String> questionIds;
    @ApiModelProperty(value = "答案集")
    private List<String> userAnswer;
}
