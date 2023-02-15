package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author MissouL
 */
@ApiModel(value = "AppUserSignInByPsdVm", description = "App用户账号密码登陆")
@Data
public class AppUserSignInByPsdVm {
    @ApiModelProperty(value = "极光id")
    private String registrationId;
    @ApiModelProperty("手机号码")
    private String phone;
    @ApiModelProperty("密码")
    private String password;
}
