package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 用于查询时间范围
 *
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "TimeVm", description = "时间段查询信息")
public class TimeVm extends PageFormVm {
    @ApiModelProperty(value = "时间开始")
    private String startTime;
    @ApiModelProperty(value = "时间结束")
    private String endTime;
}
