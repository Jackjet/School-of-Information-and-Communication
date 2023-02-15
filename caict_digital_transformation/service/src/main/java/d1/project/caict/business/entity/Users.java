package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 企业用户
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_users")
@ApiModel(value = "Users", description = "企业用户")
public class Users extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("用户名")
    private String username;

    @ApiModelProperty("密码")
    private String password;

    @ApiModelProperty("手机号")
    private String phone;

    @ApiModelProperty("企业名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("座机")
    @Column(length = 30)
    private String landline;

    @ApiModelProperty("联系人姓名")
    private String linkmanName;

    @ApiModelProperty("联系人电话")
    @Column(length = 30)
    private String linkmanPhone;

    @ApiModelProperty("省份")
    @Column(length = 30)
    private String province;

    @ApiModelProperty("市")
    @Column(length = 30)
    private String city;

    @ApiModelProperty("县")
    @Column(length = 30)
    private String county;

    @ApiModelProperty("详细地址")
    private String address;

    @ApiModelProperty("所属行业，多个用“、”分割")
    private String industries;

    @ApiModelProperty("公司LOGO")
    private String logo;

    @ApiModelProperty("客户来源")
    private String source;

    @Temporal(TemporalType.TIMESTAMP)
    @ApiModelProperty(value = "最后登录时间", example = "2018-01-23 09:12:32")
    private Calendar lastSignInTime;

    @ApiModelProperty("创建日期 yyyy-MM-dd ")
    private String registDay;

    @ApiModelProperty("创建时间 HH:mm:ss")
    private String registTime;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getLinkmanPhone() {
        return linkmanPhone;
    }

    public void setLinkmanPhone(String linkmanPhone) {
        this.linkmanPhone = linkmanPhone;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCounty() {
        return county;
    }

    public void setCounty(String county) {
        this.county = county;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIndustries() {
        return industries;
    }

    public void setIndustries(String industries) {
        this.industries = industries;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getLandline() {
        return landline;
    }

    public void setLandline(String landline) {
        this.landline = landline;
    }

    public Calendar getLastSignInTime() {
        return lastSignInTime;
    }

    public void setLastSignInTime(Calendar lastSignInTime) {
        this.lastSignInTime = lastSignInTime;
    }

    public String getRegistDay() {
        return registDay;
    }

    public void setRegistDay(String registDay) {
        this.registDay = registDay;
    }

    public String getRegistTime() {
        return registTime;
    }

    public void setRegistTime(String registTime) {
        this.registTime = registTime;
    }
}
