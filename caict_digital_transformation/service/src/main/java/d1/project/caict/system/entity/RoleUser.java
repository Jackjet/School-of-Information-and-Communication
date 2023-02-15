package d1.project.caict.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
@Entity
@Table(name = "d1_role_user")
@ApiModel(value = "RoleUser", description = "用户角色关联")
public class RoleUser extends BaseCreateEntity {

    @ApiModelProperty("角色id")
    @Column(length = 32)
    private String roleId;

    @ApiModelProperty("角色名称")
    @Column(length = 100)
    private String roleName;

    @ApiModelProperty("菜单id")
    @Column(length = 32)
    private String userId;

    @ApiModelProperty("用户名称")
    @Column(length = 100)
    private String userName;

    @ApiModelProperty("用户手机")
    @Column(length = 100)
    private String userPhone;

    @ApiModelProperty("用户账号")
    @Column(length = 100)
    private String userAccount;

    @ApiModelProperty("组织机构")
    @Column(length = 100)
    private String organizationName;


    public RoleUser() {
    }

    public RoleUser(String roleId, String roleName, WebAdminUser webAdminUser) {
        this.roleId = roleId;
        this.roleName = roleName;
        this.userId = webAdminUser.getId();
        this.userName = webAdminUser.getName();
        this.userPhone = webAdminUser.getPhone();
        this.userAccount = webAdminUser.getAccount();
        this.organizationName = webAdminUser.getOrganizationName();
    }

    public String getRoleId() {
        return roleId;
    }

    public void setRoleId(String roleId) {
        this.roleId = roleId;
    }

    public String getRoleName() {
        return roleName;
    }

    public void setRoleName(String roleName) {
        this.roleName = roleName;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(String userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserAccount() {
        return userAccount;
    }

    public void setUserAccount(String userAccount) {
        this.userAccount = userAccount;
    }

    public String getOrganizationName() {
        return organizationName;
    }

    public void setOrganizationName(String organizationName) {
        this.organizationName = organizationName;
    }
}
