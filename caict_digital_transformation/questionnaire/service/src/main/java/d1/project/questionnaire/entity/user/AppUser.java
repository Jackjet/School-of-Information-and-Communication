package d1.project.questionnaire.entity.user;

import d1.project.questionnaire.entity.base.BaseUserEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.ColumnDefault;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;


/**
 * @author Kikki 2019/8/26 10:06
 */
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "d1_app_user")
@ApiModel(value = "AppUser", description = "注册用户管理表")
@Data
public class AppUser extends BaseUserEntity {

    @ApiModelProperty(value = "真实姓名")
    private String realName;
    @ApiModelProperty(value = "极光id")
    private String registrationId;
    @ApiModelProperty(value = "微信id")
    private String weChatId;
    @ApiModelProperty(value = "介绍")
    @Lob
    private String introduce;
    @ApiModelProperty(value = "是否推送:0否1是")
    @ColumnDefault("1")
    private Boolean isPush;
    @ApiModelProperty(value = "是否注册有赞开发平台:0否1是")
    @ColumnDefault("0")
    private Boolean isYouZan;
    @ApiModelProperty(value = "有赞开发平台对外用户唯一ID")
    @Lob
    private String openUserId;
    @ApiModelProperty(value = "有赞开发平台对外用户唯一ID")
    private String yzOpenId;
    @ApiModelProperty(value = "有赞开发平台cookieKey")
    private String cookieKey;
    @ApiModelProperty(value = "有赞开发平台cookieValue")
    private String cookieValue;

    @ApiModelProperty(value = "是否是黑名单  （0 否 1 是）")
    private String isBlack;


    @Override
    public String getType() {
        return "appuser";
    }
}
