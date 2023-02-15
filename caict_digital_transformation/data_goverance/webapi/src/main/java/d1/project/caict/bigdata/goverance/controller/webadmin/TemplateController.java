package d1.project.caict.bigdata.goverance.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.Template;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.TemplateService;
import d1.project.caict.bigdata.goverance.service.rule.RuleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/template")
@Api(value = "/webadmin/template", tags = "模板表")
public class TemplateController {

    private final
    TemplateService templateService;
    private final
    RuleService ruleService;
    public TemplateController(TemplateService templateService, RuleService ruleService) {
        this.templateService = templateService;
        this.ruleService = ruleService;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody TemplateControllerInsertVm insert, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(insert);
            templateService.insert(jsonObject, request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            if (ruleService.existsByTemplateId(id)) {
                throw new Exception("该模板已被配置到规则中使用，请先在规则管理中修改相应规则！");
            }
            templateService.deleteById(id, request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody TemplateControllerUpdateVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            templateService.update(jsonObject, request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/findAll")
    public Result<Page<Template>> findAll(TemplateControllerFindAllVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            Page<Template> all = templateService.findAll(jsonObject);
            return ResultUtil.success(Constants.SUCCESS, all);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Template> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            Template byId = templateService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, byId);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "数据质量指标")
    @GetMapping(value = "/dataQualityIndex")
    @Auth("noauth")
    public Result<List<JSONObject>> dataQualityIndex() {
        try {
            List<JSONObject> byId = templateService.dataQualityIndex();
            return ResultUtil.success(Constants.SUCCESS, byId);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

@ApiModel(value = "TemplateControllerInsertVm", description = "新增")
class TemplateControllerInsertVm {
    /**
     * 唯一性
     */
    @ApiModelProperty("模板名称")
    @NotBlank(message = "模板名称不可为空")
    private String name;

    /**
     * 完整性非空约束、实体唯一性约束、有效性长度约束 、
     * 有效性标志取值约束、有效性代码值域约束、有效性内容规范约束、
     * 存在一致性依赖约束、等值一致性依赖约束 、逻辑一致性依赖约束、
     * 取值准确性约束、时效性约束
     */
    @ApiModelProperty("约束分类:完整性非空约束、实体唯一性约束、有效性长度约束 、\n" +
            "     有效性标志取值约束、有效性代码值域约束、有效性内容规范约束、\n" +
            "     存在一致性依赖约束、等值一致性依赖约束 、逻辑一致性依赖约束、\n" +
            "     取值准确性约束、时效性约束")
    @NotBlank(message = "约束分类不可为空")
    private String constraintType;

    /**
     * 技术、业务
     */
    @ApiModelProperty("业务技术分类:技术、业务")
    @NotBlank(message = "业务技术分类不可为空")
    private String businessTechnicalType;

    /**
     * 单行、多行
     */
    @ApiModelProperty("结果集:单行、多行")
    @NotBlank(message = "结果集不可为空")
    private String result;

    /**
     * 根据不同的约束分类，带出示例SQL语句
     */
    @ApiModelProperty("检查SQL")
    private String sql;

    /**
     * json格式，[
     * {
     * "变量名":"",
     * "变量含义":""
     * }
     * ]
     */
    @ApiModelProperty("变量:json类型")
    private String params;
    /**
     * 规则的编辑、删除不影响此值
     */

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getBusinessTechnicalType() {
        return businessTechnicalType;
    }

    public void setBusinessTechnicalType(String businessTechnicalType) {
        this.businessTechnicalType = businessTechnicalType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "TemplateControllerUpdateVm", description = "更新")
class TemplateControllerUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    /**
     * 完整性非空约束、实体唯一性约束、有效性长度约束 、
     * 有效性标志取值约束、有效性代码值域约束、有效性内容规范约束、
     * 存在一致性依赖约束、等值一致性依赖约束 、逻辑一致性依赖约束、
     * 取值准确性约束、时效性约束
     */
    @ApiModelProperty("约束分类:完整性非空约束、实体唯一性约束、有效性长度约束 、\n" +
            "     有效性标志取值约束、有效性代码值域约束、有效性内容规范约束、\n" +
            "     存在一致性依赖约束、等值一致性依赖约束 、逻辑一致性依赖约束、\n" +
            "     取值准确性约束、时效性约束")
    private String constraintType;

    @ApiModelProperty("数据问题分类")
    private String dataQualityType;

    /**
     * 技术、业务
     */
    @ApiModelProperty("业务技术分类:技术、业务")
    private String businessTechnicalType;

    /**
     * 单行、多行
     */
    @ApiModelProperty("结果集:单行、多行")
    private String result;

    /**
     * 根据不同的约束分类，带出示例SQL语句
     */
    @ApiModelProperty("检查SQL")
    private String sql;

    /**
     * json格式，[
     * {
     * "变量名":"",
     * "变量含义":""
     * }
     * ]
     */
    @ApiModelProperty("变量:json类型")
    private String params;
    /**
     * 规则的编辑、删除不影响此值
     */

    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getConstraintType() {
        return constraintType;
    }

    public void setConstraintType(String constraintType) {
        this.constraintType = constraintType;
    }

    public String getDataQualityType() {
        return dataQualityType;
    }

    public void setDataQualityType(String dataQualityType) {
        this.dataQualityType = dataQualityType;
    }

    public String getBusinessTechnicalType() {
        return businessTechnicalType;
    }

    public void setBusinessTechnicalType(String businessTechnicalType) {
        this.businessTechnicalType = businessTechnicalType;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getSql() {
        return sql;
    }

    public void setSql(String sql) {
        this.sql = sql;
    }

    public String getParams() {
        return params;
    }

    public void setParams(String params) {
        this.params = params;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

@ApiModel(value = "TemplateControllerFindAllVm", description = "查询列表")
class TemplateControllerFindAllVm extends PageableVm {

    @ApiModelProperty("分类名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}