package d1.project.questionnaire.entity.examination;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:54
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "ExaminationUser", description = "用户答题")
@Entity
@Table(name = "d1_examination_user")
@Data
public class ExaminationUser extends BaseCreateEntity {
    @ApiModelProperty(value = "考试id")
    private String examinationId;
    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "学习计划id")
    private String learnPlanId;
    @ApiModelProperty(value = "营期id")
    private String campPeriodId;
    @ApiModelProperty(value = "营期id")
    private String trainingCampId;
    @ApiModelProperty(value = "试题id集以,分割")
    @Lob
    private String questionIds;
    @ApiModelProperty(value = "用户答案集:每道以|分割 每个答案以,分割")
    @Lob
    private String userAnswer;
    @ApiModelProperty(value = "得分")
    private Integer userAllfraction;
    @ApiModelProperty(value = "答对数量")
    private Integer rightNumber;

}
