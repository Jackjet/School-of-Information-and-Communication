package d1.project.caict.bigdata.goverance.controller.webadmin.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.rule.RuleSubscribe;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.rule.RuleSubscribeService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;


@Auth("webadmin")
@Api(value = "/webadmin/rule/ruleSubscribe", tags = "规则订阅")
@RestController
@RequestMapping("/webadmin/rule/ruleSubscribe")
public class RuleSubscribeController {
    private final
    RuleSubscribeService ruleSubscribeService;

    public RuleSubscribeController(RuleSubscribeService ruleSubscribeService) {
        this.ruleSubscribeService = ruleSubscribeService;
    }


    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RuleSubscribeControllerInsertVm insert, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(insert);
            ruleSubscribeService.insert(jsonObject, request);
            return ResultUtil.success("订阅成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            ruleSubscribeService.deleteById(id, request);
            return ResultUtil.success("取消订阅");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情",tags = "按规则ID查找")
    @GetMapping(value = "/findByRuleId")
    public Result<RuleSubscribe> findById(@Valid @NotBlank(message = "ruleId不可为空") String ruleId, HttpServletRequest request) {
        try {
            RuleSubscribe byId = ruleSubscribeService.findByItcodeAndRuleId(ruleId, request).orElseThrow(() -> new ValidException("找不到记录:" + ruleId));
            return ResultUtil.success(Constants.SUCCESS, byId);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "列表",tags = "按当前用户查找")
    @GetMapping(value = "/findByCurrentUser")
    public Result<Page<RuleSubscribe>> findById(RuleSubscribeControllerFindAllVm model, HttpServletRequest request) {
        try {
            Page<RuleSubscribe> byCurrentUser = ruleSubscribeService.findByCurrentUser((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.SUCCESS, byCurrentUser);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

@ApiModel(value = "RuleSubscribeControllerInsertVm", description = "新增")
class RuleSubscribeControllerInsertVm {

    @ApiModelProperty("规则编码")
    private String ruleId;

    @ApiModelProperty("规则名称")
    private String ruleName;

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
}

class RuleSubscribeControllerFindAllVm extends PageableVm {

}
