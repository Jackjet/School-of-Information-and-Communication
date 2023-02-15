package d1.project.questionnaire.model.exam.exam;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class ExamReturnVm {
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "考试名")
    private String name;
    @ApiModelProperty(value = "单选提数量")
    private Long singleNum = 0L;
    @ApiModelProperty(value = "单选题总分")
    private Long singleScore = 0L;
    @ApiModelProperty(value = "多选题数量")
    private Long multipleNum = 0L;
    @ApiModelProperty(value = "多选题总分")
    private Long multipleScore = 0L;
    @ApiModelProperty(value = "总分")
    private Long totalScore = 0L;
    @ApiModelProperty(value = "试题")
    private List<ExamQuestionReturnVm> question;
}
