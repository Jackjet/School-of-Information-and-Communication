package d1.project.questionnaire.model.exam.examgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@Data
public class ExamGroupInsertVm {
    @ApiModelProperty(value = "分组名称")
    private String name;
}
