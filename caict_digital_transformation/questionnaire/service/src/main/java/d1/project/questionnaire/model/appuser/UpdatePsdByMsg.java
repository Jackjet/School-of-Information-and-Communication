package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author MissouL
 */
@ApiModel(value = "UpdatePsdByMsg", description = "短信验证,appuser修改密码")
@Data
public class UpdatePsdByMsg {
    @ApiModelProperty(value = "验证码")
    private String authCode;
    @ApiModelProperty(value = "新密码")
    private String password;
}
