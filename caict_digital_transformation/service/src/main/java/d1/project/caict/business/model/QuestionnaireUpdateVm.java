package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class QuestionnaireUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不能为空")
    private String id;

    @ApiModelProperty("调查结果")
    @NotBlank(message = "调查结果不能为空")
    private String result;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
