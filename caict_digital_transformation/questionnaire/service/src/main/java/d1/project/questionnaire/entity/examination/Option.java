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
 * @date 2020-10-12 14:47
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Option", description = "选项")
@Entity
@Table(name = "d1_option")
@Data
public class Option extends BaseCreateEntity {
    @ApiModelProperty(value = "试题id")
    private String questionId;

    @ApiModelProperty(value = "内容")
    @Lob
    private String detail;

    @ApiModelProperty(value = "序号")
    private Integer number;

    @ApiModelProperty(value = "是否正确答案->0否1是")
    private String isAnswer;

}
