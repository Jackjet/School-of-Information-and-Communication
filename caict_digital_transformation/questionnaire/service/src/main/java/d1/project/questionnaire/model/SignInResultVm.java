package d1.project.questionnaire.model;

import lombok.Data;

/**
 * 用户登录结果
 *
 * @author Kikki 2019/8/26 9:54
 */
@Data
public class SignInResultVm {
    String id;
    String token;
    String name;
    String phone;
    String cookieKey;
    String cookieValue;
}
