package d1.project.cms.entity;

import d1.framework.webapi.entity.BaseCreateEntity;
import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 管理页面端的用户entity
 *
 * @author Buter
 * @date 2020/3/15 18:05
 */

@Entity
@Table(name = "d1_web_admin_user")
@ApiModel(value = "WebAdminUser", description = "系统用户管理表")
public class WebAdminUser extends BaseCreateEntity {
    @Column(length = 20)
    @ApiModelProperty("账号")
    private String name;
    @Column(length = 20)
    @ApiModelProperty("姓名")
    private String realName;

    @Column(length = 100)
    @ApiModelProperty("密码")
    private String password;

    @Column(length = 20)
    @ApiModelProperty("性别")
    private String sex;

    @Column(length = 100)
    @ApiModelProperty("邮箱")
    private String email;

    @Column(length = 100)
    @ApiModelProperty("电话")
    private String tel;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "注册时间", example = "2018-01-23 09:12:32")
    private Calendar signUpTime;


    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "最后登录时间", example = "2018-01-23 09:12:32")
    private Calendar lastSignInTime;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public Calendar getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Calendar signUpTime) {
        this.signUpTime = signUpTime;
    }

    public Calendar getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Calendar lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }
}
