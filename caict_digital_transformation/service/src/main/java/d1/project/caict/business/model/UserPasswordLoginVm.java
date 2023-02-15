package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class UserPasswordLoginVm {
    @NotBlank(message = "手机号或用户名不能为空")
    private String phoneOrUsername;

    @NotBlank(message = "密码不能为空")
    private String password;

    public String getPhoneOrUsername() {
        return phoneOrUsername;
    }

    public void setPhoneOrUsername(String phoneOrUsername) {
        this.phoneOrUsername = phoneOrUsername;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
