package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "DeleteVm", description = "删除")
@Data
public class IdVm extends PageFormVm{
    @ApiModelProperty(value = "数据ID")
    private String id;
}
