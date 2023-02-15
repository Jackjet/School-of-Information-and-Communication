package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
public class StatisticVisitUserInsertVm {
    @NotBlank(message = "访问标识不能为空")
    @ApiModelProperty("访问标识")
    private String flag;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }
}
