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
@ApiModel(value = "AppUserUpdatePsdVm", description = "管理端App用户查询结果")
@Data
public class AppUserWebAdminFindReturnVm extends AppUser {
    @ApiModelProperty(value = "发言数量")
    private Long commentNum;
    @ApiModelProperty(value = "占卜数量")
    private Long hexagramNum;
    @ApiModelProperty(value = "笔记数量")
    private Long noteNum;

}
