package d1.project.questionnaire.entity.base;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

/**
 * @author Kikki 2019/8/29 18:47
 */
@MappedSuperclass
public abstract class BaseUserEntity extends BaseEntity {

    @Column(length = 200)
    @ApiModelProperty("用户名")
    private String name;

    @Column(length = 200)
    @ApiModelProperty("昵称")
    private String nickName;

    @Column(length = 1000)
    @ApiModelProperty("头像")
    private String avatar;

    @ApiModelProperty(value = "0男1女")
    @Column(length = 1)
    private Integer sex;

    @Column(length = 3)
    @ApiModelProperty("年龄")
    private Integer age;

    @Column
    @Temporal(TemporalType.DATE)
    @ApiModelProperty(value = "出生日期", example = "2018-01-23 09:12:32")
    private Date birthday;

    @Column(length = 30)
    @ApiModelProperty("邮箱")
    private String email;

    @Column(length = 20)
    @ApiModelProperty("手机号码")
    private String phone;

    @ApiModelProperty("地址")
    @Column(length = 80)
    private String address;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "注册时间", example = "2018-01-23 09:12:32")
    private Date signUpTime;

    @Column(length = 100)
    @ApiModelProperty("密码")
    private String password;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "最后登录时间", example = "2018-01-23 09:12:32")
    private Date lastSignInTime;

    @ApiModelProperty("授权token")
    private String accessToken;

    @ApiModelProperty("更新token")
    private String refreshToken;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "授权token过期时间", example = "2018-01-23 09:12:32")
    private Date tokenExpire;

    @ApiModelProperty("扩展字段1")
    private String ext1;

    @ApiModelProperty("扩展字段2")
    private String ext2;

    public abstract String getType();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public Integer getSex() {
        return sex;
    }

    public void setSex(Integer sex) {
        this.sex = sex;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getSignUpTime() {
        return signUpTime;
    }

    public void setSignUpTime(Date signUpTime) {
        this.signUpTime = signUpTime;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Date getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Date lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public String getRefreshToken() {
        return refreshToken;
    }

    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public Date getTokenExpire() {
        return tokenExpire;
    }

    public void setTokenExpire(Date tokenExpire) {
        this.tokenExpire = tokenExpire;
    }

    public String getExt1() {
        return ext1;
    }

    public void setExt1(String ext1) {
        this.ext1 = ext1;
    }

    public String getExt2() {
        return ext2;
    }

    public void setExt2(String ext2) {
        this.ext2 = ext2;
    }
}
