package d1.project.questionnaire.model.appuser;

import d1.project.questionnaire.model.base.PageFormVm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class AppUserFindVm extends PageFormVm {

    @ApiModelProperty(value = "查询返回类型(0:全部用户,1:昨日新增,2:上周活跃,3:黑名单用户)")
    private String typeId;

    @ApiModelProperty(value = "用户名")
    private String name;

    @ApiModelProperty(value = "手机号")
    private String phone;
}
