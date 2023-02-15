package d1.project.questionnaire.model.appuser;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author baozh
 */
@ApiModel(value = "AppUserFindTabsReturn", description = "管理端查询App用户类型返回信息")
@Data
public class AppUserFindTabsReturn {
    @ApiModelProperty(value = "查询返回类型(0:全部用户,1:昨日新增,2:上周活跃,3:黑名单)")
    private String typeId;
    @ApiModelProperty(value = "查询返回类型名")
    private String typeName;
    @ApiModelProperty(value = "数量")
    private Long num;
}
