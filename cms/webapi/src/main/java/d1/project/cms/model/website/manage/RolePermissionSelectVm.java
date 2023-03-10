package d1.project.cms.model.website.manage;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@ApiModel(value = "RolePermissionSelectVm", description = "菜单树和菜单id数组")
public class RolePermissionSelectVm {
    @ApiModelProperty(value = "菜单树")
    private List<MenuTreeGetVm> menuTree;
    @ApiModelProperty(value = "菜单id数组")
    private List<String> choose;

    public List<MenuTreeGetVm> getMenuTree() {
        return menuTree;
    }

    public void setMenuTree(List<MenuTreeGetVm> menuTree) {
        this.menuTree = menuTree;
    }

    public List<String> getChoose() {
        return choose;
    }

    public void setChoose(List<String> choose) {
        this.choose = choose;
    }
}
