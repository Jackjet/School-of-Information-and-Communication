package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * 卜卦记录管理表
 *
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "HexagramRecord", description = "卜卦记录管理表")
@Entity
@Table(name = "d1_hexagram_record")
@Data
public class HexagramRecord extends BaseCreateEntity {

    @ApiModelProperty(value = "卜卦类型 0：字组合卦单、1：铜钱摇卦卦单、2：简易规划卦单")
    @Column(length = 4)
    private String type;

    @ApiModelProperty(value = "卦象id")
    @Column(length = 20)
    private String hexagramId;

    @ApiModelProperty(value = "卜卦id")
    @Column(length = 20)
    private String hexagramChangeId;

    @ApiModelProperty(value = "卦象名称")
    @Column(length = 20)
    private String hexagramName;

    @Column(name = "keyword", length = 200)
    @ApiModelProperty(value = "起卦关键字")
    private String keyword;

    @ApiModelProperty(value = "内容")
    @Transient
    private String content;
}
