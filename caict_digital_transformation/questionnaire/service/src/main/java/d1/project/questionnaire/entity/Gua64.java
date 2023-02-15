package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "d1_gua64")
@ApiModel(value = "Gua64", description = "64挂(用户的64挂购买情况)")
@Data
public class Gua64 extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "是否免费(0:否,1:是)")
    private String isFree;
    @ApiModelProperty(value = "价格（以分计算单位）")
    private String money;
    @ApiModelProperty(value = "卦相名")
    private String name;
}
