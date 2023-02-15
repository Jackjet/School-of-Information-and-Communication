package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author MissouL
 */
@ApiModel(value = "MessageDeleteVm", description = "通知消息添加")
@Data
public class BatchDeleteVm {
    @ApiModelProperty(value = "ID列表")
    private List<String> ids;
}
