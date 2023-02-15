package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author baozh
 */
@ApiModel(value = "IdListVm", description = "批量删除")
@Data
public class IdListVm {
    @ApiModelProperty(value = "数据ID列表")
    private List<String> ids;
}
