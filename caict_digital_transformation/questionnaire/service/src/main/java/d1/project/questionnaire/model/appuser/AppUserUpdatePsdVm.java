package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author MissouL
 */
@ApiModel(value = "AppUserUpdatePsdVm", description = "appUser修改密码")
@Data
public class AppUserUpdatePsdVm {
    @ApiModelProperty(value = "用户id")
    private String id;
    @ApiModelProperty(value = "新密码")
    private String password;
    @ApiModelProperty(value = "原始密码")
    private String oldPass;
}
