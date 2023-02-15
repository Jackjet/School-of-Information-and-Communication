package d1.project.questionnaire.entity.examination;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:57
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExaminationGroup", description = "考试分组")
@Entity
@Table(name = "d1_examination_group")
@Data
public class ExaminationGroup extends BaseCreateEntity {
    @ApiModelProperty(value = "分组名称")
    private String name;

}
