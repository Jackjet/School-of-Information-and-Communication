package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author baozh
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "BrowseRecord", description = "浏览记录")
@Entity
@Table(name = "d1_browse_record")
@Data
public class BrowseRecord extends BaseCreateEntity {
    @ApiModelProperty(value = "内容ID")
    private String contentId;
}
