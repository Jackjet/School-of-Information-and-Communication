package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.NotBlank;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 10:01
 */
public class ActivityInsertVm {

    @ApiModelProperty("活动标题")
    @NotBlank(message = "活动标题不能为空")
    private String title;

    @ApiModelProperty("活动简介")
    private String synopsis;

    @ApiModelProperty(
            value = "活动开始时间",
            example = "2018-01-01 01:01:01"
    )
    @NotBlank(message = "活动开始时间不能为空")
    private String startTime;

    @ApiModelProperty(
            value = "活动结束时间",
            example = "2018-01-01 01:01:01"
    )
    @NotBlank(message = "活动结束时间不能为空")
    private String endTime;

    @ApiModelProperty("联系人姓名")
    @NotBlank(message = "活动联系人姓名不能为空")
    private String linkmanName;

    @ApiModelProperty("联系人电话")
    @NotBlank(message = "活动联系人电话不能为空")
    private String linkmanPhone;

    @ApiModelProperty("主办单位")
    @NotBlank(message = "活动主办单位不能为空")
    private String sponsor;

    @ApiModelProperty("承办单位")
    @NotBlank(message = "活动承办单位不能为空")
    private String organizer;

    @ApiModelProperty("活动内容")
    @NotBlank(message = "活动内容不能为空")
    private String content;

    @ApiModelProperty("活动图片")
    @NotBlank(message = "活动图片不能为空")
    private String pic;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentUrl;

    @ApiModelProperty("活动地址")
    @NotBlank(message = "活动地址不能为空")
    private String address;

    @ApiModelProperty("活动网址")
    private String url;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getSynopsis() {
        return synopsis;
    }

    public void setSynopsis(String synopsis) {
        this.synopsis = synopsis;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
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

    public String getSponsor() {
        return sponsor;
    }

    public void setSponsor(String sponsor) {
        this.sponsor = sponsor;
    }

    public String getOrganizer() {
        return organizer;
    }

    public void setOrganizer(String organizer) {
        this.organizer = organizer;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getPic() {
        return pic;
    }

    public void setPic(String pic) {
        this.pic = pic;
    }

    public String getAttachmentName() {
        return attachmentName;
    }

    public void setAttachmentName(String attachmentName) {
        this.attachmentName = attachmentName;
    }

    public String getAttachmentUrl() {
        return attachmentUrl;
    }

    public void setAttachmentUrl(String attachmentUrl) {
        this.attachmentUrl = attachmentUrl;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
