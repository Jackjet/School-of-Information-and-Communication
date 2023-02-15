package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author MissouL
 */
@ApiModel(value = "AppUserSignInByAuthCodeVm", description = "App用户账号验证码登陆")
@Data
public class AppUserSignInByAuthCodeVm {
    @ApiModelProperty(value = "极光id")
    private String registrationId;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("验证码")
    private String authCode;
}
