package d1.project.caict.bigdata.goverance.controller.webadmin.rule;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.rule.Rule;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.rule.RuleService;
import d1.project.caict.bigdata.goverance.service.task.TasksService;
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
@Api(value = "/webadmin/rule/rule", tags = "规则表")
@RestController
@RequestMapping("/webadmin/rule/rule")
public class RuleController {

    private final
    RuleService ruleService;
    private final
    TasksService tasksService;

    public RuleController(RuleService ruleService, TasksService tasksService) {
        this.ruleService = ruleService;
        this.tasksService = tasksService;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody RuleControllerInsertVm insert, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(insert);
            ruleService.insert(jsonObject, request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            if (tasksService.existsByRuleId(id)) {
                throw new Exception("该规则已被配置到任务中使用，请先在任务管理中修改相应任务！");
            }
            ruleService.deleteById(id, request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody RuleControllerUpdateVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            ruleService.update(jsonObject, request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/findAll")
    public Result<Page<Rule>> findAll(RuleControllerFindAllVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            Page<Rule> all = ruleService.findAll(jsonObject);
            return ResultUtil.success(Constants.SUCCESS, all);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Rule> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            Rule byId = ruleService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, byId);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

@ApiModel(value = "RuleControllerInsertVm", description = "新增")
class RuleControllerInsertVm {

    @ApiModelProperty("规则名称:唯一性")
    @NotBlank(message = "规则名称不可为空")
    private String name;

    @ApiModelProperty("技术负责人")
    private String technicalLeader;

    @ApiModelProperty("业务负责人")
    private String businessLeader;

    @ApiModelProperty("变更说明")
    private String updateMessage;

    @ApiModelProperty("模板编码:模板表的主键id")
    @NotBlank(message = "模板编码不可为空")
    private String templateId;

    @ApiModelProperty("模板名称")
    @NotBlank(message = "模板名称不可为空")
    private String templateName;

    @ApiModelProperty("变量:json格式，[\n" +
            "    {\n" +
            "        \"变量名\":\"\",\n" +
            "        \"变量含义\":\"\",\n" +
            "        \"值\":\"\"\n" +
            "    }\n" +
            "]")
    private String params;

    @ApiModelProperty("关联工单")
    private Integer workOrderCount;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("检查SQL")
    private String sql;

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTechnicalLeader() {
        return technicalLeader;
    }

    public void setTechnicalLeader(String technicalLeader) {
        this.technicalLeader = technicalLeader;
    }

    public String getBusinessLeader() {
        return businessLeader;
    }

    public void setBusinessLeader(String businessLeader) {
        this.businessLeader = businessLeader;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getWorkOrderCount() {
        return workOrderCount;
    }

    public void setWorkOrderCount(Integer workOrderCount) {
        this.workOrderCount = workOrderCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "RuleControllerUpdateVm", description = "更新")
class RuleControllerUpdateVm {
    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("规则名称:唯一性")
    @NotBlank(message = "规则名称不可为空")
    private String name;

    @ApiModelProperty("检查SQL")
    private String sql;

    @ApiModelProperty("技术负责人")
    private String technicalLeader;

    @ApiModelProperty("业务负责人")
    private String businessLeader;

    @ApiModelProperty("变更说明")
    private String updateMessage;

    @ApiModelProperty("模板编码:模板表的主键id")
    @NotBlank(message = "模板编码不可为空")
    private String templateId;

    @ApiModelProperty("模板名称")
    @NotBlank(message = "模板名称不可为空")
    private String templateName;

    @ApiModelProperty("变量:json格式，[\n" +
            "    {\n" +
            "        \"变量名\":\"\",\n" +
            "        \"变量含义\":\"\",\n" +
            "        \"值\":\"\"\n" +
            "    }\n" +
            "]")
    private String params;

    @ApiModelProperty("关联工单")
    private Integer workOrderCount;

    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getTechnicalLeader() {
        return technicalLeader;
    }

    public void setTechnicalLeader(String technicalLeader) {
        this.technicalLeader = technicalLeader;
    }

    public String getBusinessLeader() {
        return businessLeader;
    }

    public void setBusinessLeader(String businessLeader) {
        this.businessLeader = businessLeader;
    }

    public String getUpdateMessage() {
        return updateMessage;
    }

    public void setUpdateMessage(String updateMessage) {
        this.updateMessage = updateMessage;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public Integer getWorkOrderCount() {
        return workOrderCount;
    }

    public void setWorkOrderCount(Integer workOrderCount) {
        this.workOrderCount = workOrderCount;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }


}

@ApiModel(value = "RuleControllerFindAllVm", description = "查询列表")
class RuleControllerFindAllVm extends PageableVm {

    @ApiModelProperty("name")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

}