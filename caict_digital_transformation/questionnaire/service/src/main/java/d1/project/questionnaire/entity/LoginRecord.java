package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;


/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "d1_login_record")
@ApiModel(value = "LoginRecord", description = "登录记录表")
@Data
public class LoginRecord extends BaseCreateEntity {
}
