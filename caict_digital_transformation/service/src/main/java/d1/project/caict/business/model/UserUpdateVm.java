package d1.project.caict.business.model;

import javax.validation.constraints.NotBlank;

/**
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class UserUpdateVm {
    @NotBlank(message = "用户id不能为空")
    private String id;

    @NotBlank(message = "企业名称不能为空")
    private String name;

    @NotBlank(message = "联系人姓名不能为空")
    private String linkmanName;

    @NotBlank(message = "联系人电话不能为空")
    private String linkmanPhone;

    @NotBlank(message = "省份不能为空")
    private String province;

    @NotBlank(message = "市不能为空")
    private String city;

    @NotBlank(message = "县不能为空")
    private String county;

    @NotBlank(message = "详细地址不能为空")
    private String address;

    @NotBlank(message = "所属行业不能为空")
    private String industries;

    private String logo;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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
}
