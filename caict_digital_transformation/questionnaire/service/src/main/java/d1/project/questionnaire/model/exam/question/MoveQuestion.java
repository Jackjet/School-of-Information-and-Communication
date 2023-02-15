package d1.project.questionnaire.model.exam.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 移动试题
 *
 * @author baozh
 */
@Data
public class MoveQuestion {
    @ApiModelProperty(value = "试题ID")
    private List<String> ids;
    @ApiModelProperty(value = "考试分组id")
    private String examinationGroupId;
}
