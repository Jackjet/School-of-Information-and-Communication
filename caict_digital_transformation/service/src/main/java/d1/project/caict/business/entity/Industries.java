package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 行业
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_industries")
@ApiModel(value = "Industries", description = "行业")
public class Industries extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("英文名")
    private String englishName;

    @ApiModelProperty("行业名称")
    private String name;

    public String getEnglishName() {
        return englishName;
    }

    public void setEnglishName(String englishName) {
        this.englishName = englishName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
