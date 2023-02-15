package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Article", description = "相关文章")
@Entity
@Table(name = "d1_article")
@Data
public class Article extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "卦象ID")
    @Lob
    private String hexagramId;

    @ApiModelProperty(value = "卦象名")
    @Lob
    private String hexagramName;

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "封面图")
    private String cover;

    @ApiModelProperty(value = "详情")
    @Lob
    private String detail;

    @ApiModelProperty(value = "作者姓名")
    private String authorName;

    @ApiModelProperty(value = "外部连接")
    @Column(length = 1000)
    private String externalLinks;

    @ApiModelProperty(value = "分享内容")
    @Lob
    private String shareContent;

    @Transient
    @ApiModelProperty(value = "阅读数量")
    private Long readNum = 0L;

}
