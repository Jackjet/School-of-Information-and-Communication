package d1.project.questionnaire.entity.user;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "d1_app_user_course")
@ApiModel(value = "AppUserCourse", description = "用户课程扩展")
@Data
public class AppUserCourse extends BaseCreateEntity {
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "学号")
    private String studentNumber;

}
