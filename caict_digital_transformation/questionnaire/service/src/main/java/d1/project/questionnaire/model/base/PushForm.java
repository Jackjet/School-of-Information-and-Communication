package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/***
 * 推送消息
 * @author MissouL
 */
@ApiModel(value = "PushForm", description = "推送消息")
@Data
public class PushForm {
    @ApiModelProperty(value = "消息ID")
    private String id;
}
