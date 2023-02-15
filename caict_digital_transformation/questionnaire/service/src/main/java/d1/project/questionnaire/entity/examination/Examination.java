package d1.project.questionnaire.entity.examination;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
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
 * @date 2020-10-12 14:44
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Examination", description = "考试")
@Entity
@Table(name = "d1_examination")
@Data
public class Examination extends BaseCreateAndUpdate {

    @ApiModelProperty(value = "考试名称")
    private String name;
    @ApiModelProperty(value = "总分")
    private Integer totalSorce;
//    @ApiModelProperty(value = "单选分值")
//    private Integer singleChoice;
//    @ApiModelProperty(value = "多选分值")
//    private Integer multipleChoice;


}
