package d1.project.caict.bigdata.goverance.entity;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "d1_group_web_admin_user")
@ApiModel(value = "GroupWebAdminUser", description = "用户组名")
public class GroupWebAdminUser extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("描述")
    private String description;


    @ApiModelProperty("创建人名称")
    private String createByName;

    @ApiModelProperty("修改人名称")
    private String updateByName;

    public String getCreateByName() {
        return createByName;
    }

    public void setCreateByName(String createByName) {
        this.createByName = createByName;
    }

    public String getUpdateByName() {
        return updateByName;
    }

    public void setUpdateByName(String updateByName) {
        this.updateByName = updateByName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
