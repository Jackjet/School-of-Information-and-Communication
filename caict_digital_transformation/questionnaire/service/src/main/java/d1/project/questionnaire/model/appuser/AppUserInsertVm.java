package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @author MissouL
 */
@ApiModel(value = "AppUserInsertVm", description = "App注册用户")
@Data
public class AppUserInsertVm {
    @ApiModelProperty(value = "电话号")
    private String phone;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "邀请码")
    private String inviteCode;

    @ApiModelProperty(value = "验证码")
    private String authCode;

    @ApiModelProperty(value = "极光id")
    private String registrationId;

    @ApiModelProperty(value = "手机类型(1:安卓,2:IOS,3:其他手机)")
    private String phoneType;
}
