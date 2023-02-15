package d1.project.questionnaire.entity;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Date;

/***
 * 短信记录
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Sms", description = "短信记录表")
@Data
public class Sms extends BaseEntity {

    @ApiModelProperty(value = "电话号")
    private String phone;

    @ApiModelProperty(value = "验证码")
    private String authCode;

    @ApiModelProperty(value = "添加时间")
    private Date createTime;

    @ApiModelProperty(value = "尝试次数")
    private int count;

    @ApiModelProperty(value = "失效")
    private boolean exist;

    @ApiModelProperty(value = "验证码类型")
    private int type;


}
