package d1.project.questionnaire.model.exam.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@Data
public class OptionInsertVm {

    @ApiModelProperty(value = "内容")
    private String detail;

    @ApiModelProperty(value = "序号")
    private Integer number;

    @ApiModelProperty(value = "是否正确答案->0否1是")
    private String isAnswer;
}
