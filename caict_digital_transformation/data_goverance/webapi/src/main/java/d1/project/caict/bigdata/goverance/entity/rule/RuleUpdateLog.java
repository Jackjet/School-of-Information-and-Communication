package d1.project.caict.bigdata.goverance.entity.rule;

import d1.framework.webapi.entity.BaseEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.Calendar;

@Entity
@Table(name = "d1_rule_update_log")
@ApiModel(value = "RuleUpdateLog", description = "规则变更历史表")
public class RuleUpdateLog extends BaseEntity {

    @ApiModelProperty("规则编码")
    private String ruleId;

    @ApiModelProperty("变更日志")
    private String log;

    @ApiModelProperty("变更时间")
    private Calendar updateTime;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }

    public String getLog() {
        return log;
    }

    public void setLog(String log) {
        this.log = log;
    }

    public Calendar getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Calendar updateTime) {
        this.updateTime = updateTime;
    }
}
