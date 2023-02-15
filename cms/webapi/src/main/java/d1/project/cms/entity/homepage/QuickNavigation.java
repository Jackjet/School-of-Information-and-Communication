package d1.project.cms.entity.homepage;

import d1.framework.webapi.entity.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 快捷导航栏
 * @author baozh
 */

@Entity
@Table(name = "d1_quick_navigation")
@ApiModel(value = "QuickNavigation", description = "快捷导航")
public class QuickNavigation extends BaseCreateEntity {

    @ApiModelProperty("名称")
    private String apiName;

    @ApiModelProperty("地址路径")
    private String apiUrl;

    @ApiModelProperty("用户ID")
    private String userId;

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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

}

