package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@ApiModel(value = "CountVm", description = "统计信息")
@Data
public class CountVm {
    @ApiModelProperty(value = "类型")
    private String name;
    @ApiModelProperty(value = "数量")
    private Long value;
}
