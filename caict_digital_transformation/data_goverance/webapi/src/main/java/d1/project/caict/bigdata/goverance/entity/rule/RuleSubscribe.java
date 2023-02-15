package d1.project.caict.bigdata.goverance.entity.rule;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "d1_rule_subscribe")
@ApiModel(value = "RuleSubscribe", description = "规则订阅表")
public class RuleSubscribe extends BaseEntity {

    @ApiModelProperty("规则编码")
    private String ruleId;

    @ApiModelProperty("规则名称")
    private String ruleName;

    @ApiModelProperty("用户id")
    private String itcode;

    @ApiModelProperty("创建时间")
    private Calendar createTime;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }

    public String getItcode() {
        return itcode;
    }

    public void setItcode(String itcode) {
        this.itcode = itcode;
    }

    public Calendar getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Calendar createTime) {
        this.createTime = createTime;
    }
}
