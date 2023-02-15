package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.math.BigDecimal;
import java.util.Date;

/***
 * 支付记录表
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "PayRecord", description = "支付记录表")
@Entity
@Table(name = "d1_pay_record")
@Data
public class PayRecord extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "卦象id")
    @Column(length = 20)
    private String hexagramId;

    @ApiModelProperty(value = "卦象名称")
    @Column(length = 20)
    private String hexagramName;

    @ApiModelProperty(value = "订单状态 0：支付成功、1：支付失败、2：待支付")
    @Column(length = 10)
    private String status;

    @ApiModelProperty(value = "支付金额(按 分 计算)")
    private BigDecimal money;

    @Column
    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "支付时间")
    private Date payTime;

    @Column
    @ApiModelProperty(value = "微信支付单号id")
    private String transactionId;

}
