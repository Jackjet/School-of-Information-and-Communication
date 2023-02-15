package d1.project.questionnaire.model.appuser;

import d1.project.questionnaire.entity.user.AppUser;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "AppUserFindByIdReturnVm", description = "App用户信息返回")
@Data
public class AppUserFindByIdReturnVm extends AppUser {
    @ApiModelProperty(value = "收藏数量")
    private Long collectNum;
    @ApiModelProperty(value = "笔记数量")
    private Long noteNum;
    @ApiModelProperty(value = "评论数量")
    private Long commentNum;
    @ApiModelProperty(value = "卜卦记录数量")
    private Long recordNum;
    @ApiModelProperty(value = "通知消息(0：无未读，1：有未读)")
    private String message;
    @ApiModelProperty(value = "未读通知数量")
    private Long msgNum;
    @ApiModelProperty(value = "未读通知数量")
    private String studentNumber;

}
