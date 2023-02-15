package d1.project.questionnaire.model.exam.exam;

import d1.project.questionnaire.entity.examination.Option;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class ExamQuestionReturnVm {
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "序号")
    private Integer number;
    @ApiModelProperty(value = "(分类)0单选1多选")
    private String type;
    @ApiModelProperty(value = "题目")
    private String topic;
    @ApiModelProperty(value = "分数")
    private Integer score;
    @ApiModelProperty(value = "选项")
    private List<Option> option;
}
