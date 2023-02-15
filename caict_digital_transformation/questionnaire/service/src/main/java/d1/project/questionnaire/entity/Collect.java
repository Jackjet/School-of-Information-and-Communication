package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Transient;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Collect", description = "我的收藏")
@Entity
@Table(name = "d1_collect")
@Data
public class Collect extends BaseCreateEntity {
    @ApiModelProperty(value = "类型:  1.轮播图 2.手动选卦象,3周易卦序,4八宫排序,5易友笔记,6每日一读,7卦序相关,8相关文章,9逐爻选挂")
    private String type;
    @ApiModelProperty(value = "标题")
    private String title;
    @ApiModelProperty(value = "内容id")
    private String contentId;
    @ApiModelProperty(value = "图片路径")
    private String imgPath;
    @Transient
    @ApiModelProperty(value = "阅读数量")
    private Long readNum = 0L;
    @Transient
    @ApiModelProperty(value = "图片路径1")
    private String cover1;
    @Transient
    @ApiModelProperty(value = "图片路径2")
    private String cover2;
}
