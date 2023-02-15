package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.Date;

/**
 * @author MissouL
 */
@ApiModel(value = "AppUserUpdateMsgVm", description = "App用户修改基本信息")
@Data
public class AppUserUpdateMsgVm {
    @ApiModelProperty(value = "id")
    private String id;

    @ApiModelProperty(value = "真实姓名")
    private String realName;

    @ApiModelProperty(value = "介绍")
    private String introduce;

    @ApiModelProperty(value = "0男1女")
    private Integer sex;

    @ApiModelProperty("用户名")
    private String name;

    @ApiModelProperty("昵称")
    private String nickName;

    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty("年龄")
    private Integer age;

    @ApiModelProperty(value = "出生日期", example = "2018-01-23 09:12:32")
    private Date birthday;

    @ApiModelProperty("邮箱")
    private String email;

    @ApiModelProperty("地址")
    private String address;
}
