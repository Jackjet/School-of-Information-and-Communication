package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.*;
import java.util.Calendar;

/**
 * 活动
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_activities")
@ApiModel(value = "Activities", description = "活动")
public class Activities extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("活动标题")
    private String title;

    @ApiModelProperty("活动简介")
    private String synopsis;

    @ApiModelProperty(
            value = "活动开始时间",
            example = "2018-01-01 01:01:01"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar startTime;

    @ApiModelProperty(
            value = "活动结束时间",
            example = "2018-01-01 01:01:01"
    )
    @Temporal(TemporalType.TIMESTAMP)
    private Calendar endTime;

    @ApiModelProperty("联系人姓名")
    private String linkmanName;

    @ApiModelProperty("联系人电话")
    @Column(length = 30)
    private String linkmanPhone;

    @ApiModelProperty("主办单位")
    private String sponsor;

    @ApiModelProperty("承办单位")
    private String organizer;

    @ApiModelProperty("活动内容")
    @Column(columnDefinition = "TEXT")
    private String content;

    @ApiModelProperty("活动图片")
    private String pic;

    @ApiModelProperty("附件名称")
    private String attachmentName;

    @ApiModelProperty("附件地址")
    private String attachmentUrl;

    @ApiModelProperty("活动地址")
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

    public Calendar getStartTime() {
        return startTime;
    }

    public void setStartTime(Calendar startTime) {
        this.startTime = startTime;
    }

    public Calendar getEndTime() {
        return endTime;
    }

    public void setEndTime(Calendar endTime) {
        this.endTime = endTime;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
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
}
