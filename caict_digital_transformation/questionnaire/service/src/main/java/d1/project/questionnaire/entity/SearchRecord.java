package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "SearchRecord", description = "搜索记录")
@Entity
@Table(name = "d1_search_record")
@Data
public class SearchRecord extends BaseCreateEntity {
    @ApiModelProperty(value = "搜索内容")
    private String searchContent;
}
