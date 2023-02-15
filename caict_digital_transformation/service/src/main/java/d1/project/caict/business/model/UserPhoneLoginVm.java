package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class UserPhoneLoginVm {
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotBlank(message = "验证码不能为空")
    private String code;

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
}
