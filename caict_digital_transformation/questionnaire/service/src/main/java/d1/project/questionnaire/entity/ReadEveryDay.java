package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@Data
@ApiModel(value = "ReadEveryDay", description = "每日一读")
@Entity
@Table(name = "d1_read_every_day")
public class ReadEveryDay extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "封面图")
    private String cover;
    @ApiModelProperty(value = "详情")
    @Column(columnDefinition = "longtext")
    private String detail;
    @ApiModelProperty(value = "外部连接")
    @Column(columnDefinition = "longtext")
    private String externalLinks;
    @ApiModelProperty(value = "作者姓名")
    private String authorName;
    @ApiModelProperty(value = "分享内容")
    @Column(columnDefinition = "longtext")
    private String shareContent;
    @ApiModelProperty(value = "状态(1.发布中,2.待发布,3.草稿,4.已下架,5.待定)")
    private String status;
    @ApiModelProperty(value = " 发布时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedTime;
    @ApiModelProperty(value = "下架时间")
    @Temporal(TemporalType.TIMESTAMP)
    private Date shelfTime;
    @ApiModelProperty(value = "备注")
    private String remarks;
    @ApiModelProperty(value = "是否加精（0否 1是）")
    private String isGreat;
    @ApiModelProperty(value = "是否头条（0否 1是）")
    private String isTop;
    @Transient
    @ApiModelProperty(value = "阅读数量")
    private Long readNum = 0L;
    @Transient
    @ApiModelProperty(value = "评论数量")
    private Long commentNum = 0L;
}
