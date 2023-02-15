package d1.project.caict.business.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 调查问卷
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Entity
@Table(name = "d1_questionnaires")
@ApiModel(value = "Questionnaires", description = "调查问卷")
public class Questionnaires extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("用户Id")
    private String userId;

    @ApiModelProperty("企业名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("问卷类型")
    private String type;

    @ApiModelProperty("调查结果")
    @Column(columnDefinition = "TEXT")
    private String result;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
