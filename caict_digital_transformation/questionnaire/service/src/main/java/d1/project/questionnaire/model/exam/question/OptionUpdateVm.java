package d1.project.questionnaire.model.exam.question;


import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class OptionUpdateVm extends OptionInsertVm {
    @ApiModelProperty(value = "ID")
    private String id;
}
