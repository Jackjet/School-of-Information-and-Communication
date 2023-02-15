package d1.project.questionnaire.entity.user;

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
 * @date 2020-10-15 16:21
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "UserAddress", description = "用户收货地址")
@Entity
@Table(name = "d1_user_address")
@Data
public class UserAddress extends BaseCreateEntity {

    @ApiModelProperty(value = "用户id")
    private String userId;
    @ApiModelProperty(value = "收货人姓名")
    private String consigneeName;
    @ApiModelProperty(value = "联系方式")
    private String consigneePhone;
    @ApiModelProperty(value = "省")
    private String province;
    @ApiModelProperty(value = "市")
    private String city;
    @ApiModelProperty(value = "区")
    private String area;
    @ApiModelProperty(value = "收货人地址")
    private String address;
    @ApiModelProperty(value = "物流信息")
    private String wechatId;


}
