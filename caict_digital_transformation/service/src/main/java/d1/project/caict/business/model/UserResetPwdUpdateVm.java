package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * @author: maorui
 * @date: 2020/9/25
 */
public class UserResetPwdUpdateVm {

    @NotBlank(message = "id不能为空")
    private String id;

    @NotBlank(message = "密码不能为空")
    private String password;

    private String plaintext;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPlaintext() {
        return plaintext;
    }

    public void setPlaintext(String plaintext) {
        this.plaintext = plaintext;
    }
}
