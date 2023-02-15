package d1.project.questionnaire.model.exam.examination;

import d1.project.questionnaire.model.base.PageFormVm;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 15:49
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class ExaminationFindAllVm extends PageFormVm {
    @ApiModelProperty(value = "名称")
    private String name;
}
