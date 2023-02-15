package d1.project.cms.model.homepage;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * @author baozh
 */
public class InsertVm {
    @NotBlank(message = "导航名称不可为空")
    @ApiModelProperty(value = "导航名称")
    private String apiName;
    @NotBlank(message = "地址路径不能为空")
    @ApiModelProperty(value = "地址路径")
    private String apiUrl;

    public String getApiName() {
        return apiName;
    }

    public void setApiName(String apiName) {
        this.apiName = apiName;
    }

    public String getApiUrl() {
        return apiUrl;
    }

    public void setApiUrl(String apiUrl) {
        this.apiUrl = apiUrl;
    }
}
