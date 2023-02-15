package d1.project.caict.bigdata.goverance.controller.webadmin.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.rule.RuleUpdateLog;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.rule.RuleUpdateLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Auth("webadmin")
@Api(value = "/webadmin/rule/ruleUpdateLog", tags = "规则变更历史")
@RestController
@RequestMapping("/webadmin/rule/ruleUpdateLog")
public class RuleUpdateLogController {
    private final
    RuleUpdateLogService ruleUpdateLogService;

    public RuleUpdateLogController(RuleUpdateLogService ruleUpdateLogService) {
        this.ruleUpdateLogService = ruleUpdateLogService;
    }


    @ApiOperation(value = "查询")
    @GetMapping(value = "/findAll")
    public Result<Page<RuleUpdateLog>> findAll(RuleUpdateLogControllerFindAllVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            Page<RuleUpdateLog> all = ruleUpdateLogService.findAll(jsonObject);
            return ResultUtil.success(Constants.SUCCESS, all);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

@ApiModel(value = "RuleUpdateLogControllerFindAllVm", description = "列表查询")
class RuleUpdateLogControllerFindAllVm extends PageableVm {

    @ApiModelProperty("规则编码")
    private String ruleId;

    public String getRuleId() {
        return ruleId;
    }

    public void setRuleId(String ruleId) {
        this.ruleId = ruleId;
    }
}