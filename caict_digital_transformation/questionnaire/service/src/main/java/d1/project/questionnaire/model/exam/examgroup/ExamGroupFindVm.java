package d1.project.questionnaire.model.exam.examgroup;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author baozh
 */
@Data
public class ExamGroupFindVm {
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "分组名称")
    private String name;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "试题数量")
    private Long questionNumber;
}
