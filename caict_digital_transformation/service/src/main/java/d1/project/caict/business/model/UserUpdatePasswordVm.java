package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class UserUpdatePasswordVm {
    @NotBlank(message = "用户手机号不能为空")
    private String phone;

    @NotBlank(message = "手机验证码不能为空")
    private String code;

    @NotBlank(message = "新密码不能为空")
    private String password;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
