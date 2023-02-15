package d1.project.questionnaire.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@ApiModel(value = "UserMsg", description = "修改密码")
@Data
public class UserMsg {
    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "新密码")
    private String newPwd;
}
