package d1.project.questionnaire.model.exam.question;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@Data
public class QuestionInsertVm {
    @ApiModelProperty(value = "(分类)0单选1多选")
    private String type;
    @ApiModelProperty(value = "题目")
    private String topic;
    @ApiModelProperty(value = "问题解析")
    private String problemAnalysis;
    @ApiModelProperty(value = "选项")
    private List<OptionInsertVm> option;
    @ApiModelProperty(value = "分组id")
    private String examinationGroupId;
}
