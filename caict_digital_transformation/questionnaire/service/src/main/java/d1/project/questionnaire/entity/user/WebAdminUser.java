package d1.project.questionnaire.entity.user;

import d1.framework.webapi.entity.DoUserBaseEntity;
import io.swagger.annotations.ApiModel;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Kikki 2019/8/26 10:06
 */
@Entity
@Table(name = "d1_web_admin_user")
@ApiModel(value = "WebAdminUser", description = "系统用户管理表")
public class WebAdminUser extends DoUserBaseEntity {

    @Override
    public String getType() {
        return "webadmin";
    }
}
