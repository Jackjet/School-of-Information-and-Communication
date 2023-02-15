package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class QuestionnaireInsertVm {

    @ApiModelProperty("企业id")
    @NotBlank(message = "企业id不能为空")
    private String userId;

    @ApiModelProperty("企业名称")
    @NotBlank(message = "企业名称不能为空")
    private String name;

    @ApiModelProperty("问卷类型")
    @NotBlank(message = "问卷类型不能为空")
    private String type;

    @ApiModelProperty("调查结果")
    @NotBlank(message = "调查结果不能为空")
    private String result;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
