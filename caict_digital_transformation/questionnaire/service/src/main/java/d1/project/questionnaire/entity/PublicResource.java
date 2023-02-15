package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PublicResource", description = "公共资源表")
@Entity
@Table(name = "d1_public_resource")
@Data
public class PublicResource extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "类型(0:封面图,1:安卓,2:IOS)")
    private String type;
    @ApiModelProperty(value = "（0:使用，1：未使用）")
    private String isUsed;
    @ApiModelProperty(value = "用途描述")
    private String description;
    @ApiModelProperty(value = "详情")
    private String detail;
}
