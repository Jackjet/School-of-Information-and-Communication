package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HexagramYaoRecord", description = "逐爻卜卦记录管理表")
@Entity
@Table(name = "d1_hexagram_yao_record")
@Data
public class HexagramYaoRecord extends BaseCreateEntity {

    @ApiModelProperty(value = "卦象id")
    @Column(length = 20)
    private String hexagramId;

    @ApiModelProperty(value = "卦象名称")
    @Column(length = 20)
    private String hexagramName;

}
