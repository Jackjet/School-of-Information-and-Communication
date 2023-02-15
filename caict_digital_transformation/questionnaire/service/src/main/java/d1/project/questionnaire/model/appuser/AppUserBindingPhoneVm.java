package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author MissouL
 */
@ApiModel(value = "AppUserBindingPhoneVm", description = "App用户绑定手机")
@Data
public class AppUserBindingPhoneVm {
    @ApiModelProperty(value = "手机号")
    private String phone;
    @ApiModelProperty(value = "验证码")
    private String authCode;
    @ApiModelProperty(value = "邀请码")
    private String inviteCode;
    @ApiModelProperty(value = "手机类型(1:安卓,2:IOS,3:其他手机)")
    private String phoneType;
}
