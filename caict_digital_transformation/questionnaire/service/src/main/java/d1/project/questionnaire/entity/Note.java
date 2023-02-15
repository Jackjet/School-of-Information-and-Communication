package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 笔记管理表
 *
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Note", description = "笔记管理表")
@Entity
@Table(name = "d1_note")
@Data
public class Note extends BaseCreateAndUpdate {

    @ApiModelProperty(value = "标题")
    private String title;

    @ApiModelProperty(value = "卦象id")
    @Column(length = 20)
    private String hexagramId;

    @ApiModelProperty(value = "卜卦id")
    @Column(length = 20)
    private String hexagramChangeId;

    @ApiModelProperty(value = "卦相名")
    private String hexagramName;

    @ApiModelProperty(value = "笔记类型 0：学卦、1：卜卦")
    @Column(length = 1)
    private String type;

    @ApiModelProperty(value = "卜卦类型 0：本卦、1：变卦")
    @Column(length = 1)
    private String hexagramType;

    @ApiModelProperty(value = "卜卦类型 0：字组合卦单、1：铜钱摇卦卦单、2：简易规划卦单")
    private String hexagramNoteType;

    @ApiModelProperty(value = "笔记类型:子类型 1:视频解析、2:文字解析（PS:总挂为null）")
    private String typeSubtypes;

    @ApiModelProperty(value = "笔记类型:子类型id")
    private String typeSubtypesId;

    @ApiModelProperty(value = "typeSubtypes类型里的类型:孙子类型 ")
    private String typeGrandsonId;

    @ApiModelProperty(value = "笔记内容")
    @Column(columnDefinition = "longtext")
    private String content;

}