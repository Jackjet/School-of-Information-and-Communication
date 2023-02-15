package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuaa
 */
@ApiModel(value = "UpdateIsBlackVm", description = "黑名单编辑状态")
@Data
public class UpdateIsBlackVm {
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "是否是黑名单  （0 否 1 是）")
    private String isBlack;
}
