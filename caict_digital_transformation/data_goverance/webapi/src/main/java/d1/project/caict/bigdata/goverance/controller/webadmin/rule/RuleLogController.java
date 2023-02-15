package d1.project.caict.bigdata.goverance.controller.webadmin.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.rule.RuleLog;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.rule.RuleLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/ruleLog")
@Api(value = "/webadmin/ruleLog", tags = "规则日志管理")
public class RuleLogController {
    private final RuleLogService ruleLogService;

    @Autowired
    public RuleLogController(RuleLogService ruleLogService){
        this.ruleLogService=ruleLogService;
    }

    @ApiOperation(value = "查询规则日志信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<RuleLog>> findAll(RuleLogGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, ruleLogService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<RuleLog> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            RuleLog taskLog = ruleLogService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, taskLog);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

class RuleLogGetVm extends PageableVm {
    @ApiModelProperty("规则名称")
    private String ruleName;

    public String getRuleName() {
        return ruleName;
    }

    public void setRuleName(String ruleName) {
        this.ruleName = ruleName;
    }
}
