package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Kikki 2019/8/30 9:09
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Carousel", description = "轮播图管理表")
@Entity
@Table(name = "d1_carousel")
@Data
public class Carousel extends BaseCreateAndUpdate {

    @ApiModelProperty(value = "类型 0:合集、1:占卜、2:分类、3:个人中心,4:课堂页,5:如何上课,6购课咨询")
    @Column(length = 1)
    private String type;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "详情")
    @Lob
    private String detail;

    @ApiModelProperty(value = "外部连接")
    @Column(length = 1000)
    private String externalLinks;

    @ApiModelProperty(value = "分享内容")
    @Lob
    private String shareContent;

    @ApiModelProperty(value = "序号")
    private Integer serialNumber;
}
