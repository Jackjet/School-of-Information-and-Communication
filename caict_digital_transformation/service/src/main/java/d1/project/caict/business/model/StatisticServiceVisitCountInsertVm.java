package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author maorui
 */
public class StatisticServiceVisitCountInsertVm {

    @NotBlank(message = "访问标识不能为空")
    @ApiModelProperty("访问标识")
    private String flag;

    @NotBlank(message = "服务类型不能为空")
    @ApiModelProperty("服务类型")
    private String type;

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
