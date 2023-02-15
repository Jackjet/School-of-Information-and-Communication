package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@ApiModel(value = "AppUserAuthCodeCheckVm", description = "AppUser验证码验证手机号")
@Data
public class AppUserAuthCodeCheckVm {
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "验证码")
    private String authCode;
}
