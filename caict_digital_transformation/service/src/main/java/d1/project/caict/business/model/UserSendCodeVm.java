package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class UserSendCodeVm {
    @NotBlank(message = "手机号不能为空")
    private String phone;

    @NotNull(message = "验证码类型不能为空")
    private int type;

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }
}
