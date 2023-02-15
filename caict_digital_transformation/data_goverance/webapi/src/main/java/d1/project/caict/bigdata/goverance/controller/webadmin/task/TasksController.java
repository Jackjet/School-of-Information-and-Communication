package d1.project.caict.bigdata.goverance.controller.webadmin.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.task.Tasks;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.task.TasksService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/tasks")
@Api(value = "/webadmin/tasks", tags = "任务管理")
public class TasksController {
    private final TasksService tasksService;
    final
    WebAdminService webAdminService;
    @Autowired
    public TasksController(TasksService tasksService, WebAdminService webAdminService) {
        this.tasksService = tasksService;
        this.webAdminService = webAdminService;
    }

    @ApiOperation(value = "查询任务信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Tasks>> findAll(TasksGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, tasksService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<Tasks> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            Tasks tasks = tasksService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, tasks);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加任务信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@Valid @RequestBody TasksInsertVm model, HttpServletRequest request) {
        try {
            tasksService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新任务信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody TasksUpdateVm model, HttpServletRequest request) {
        try {
            tasksService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除任务信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            tasksService.deleteById(id, request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新任务状态信息")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
    public Result<String> updateStatus(@Valid @RequestBody TasksUpdateStatusVm model, HttpServletRequest request) {
        try {
            tasksService.updateStatus((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.OPERATION_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "手动执行任务")
    @RequestMapping(value = "/execute", method = RequestMethod.PUT)
    public Result<String> execute(@Valid @RequestBody TaskManualOperationVm model, HttpServletRequest request){
        try {
            WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
            tasksService.execute(model.getId(), currentWebadminUserEntity.getId(),currentWebadminUserEntity.getName(),null);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class TasksGetVm extends PageableVm {
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("增全量策略,增量、全量")
    private String strategy;
    @ApiModelProperty("状态")
    private String status;

    public String getGroupTaskId() {
        return groupTaskId;
    }

    public void setGroupTaskId(String groupTaskId) {
        this.groupTaskId = groupTaskId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class TasksInsertVm{
    @NotBlank(message = "任务组id不能为空")
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @NotBlank(message = "任务组名称不能为空")
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty("名称")
    private String name;
    @NotBlank(message = "数据来源id不能为空")
    @ApiModelProperty("数据来源id")
    private String dataSourceSourceId;
    @NotBlank(message = "数据来源名称不能为空")
    @ApiModelProperty("数据来源名称")
    private String dataSourceSourceName;
    @NotBlank(message = "目标源id不能为空")
    @ApiModelProperty("目标源id")
    private String dataSourceTargetId;
    @NotBlank(message = "目标源名称不能为空")
    @ApiModelProperty("目标源名称")
    private String dataSourceTargetName;
    @ApiModelProperty("源表")
    private String sourceTable;
    @ApiModelProperty("目标表")
    private String targetTable;
    @ApiModelProperty("增全量策略,增量、全量")
    private String strategy;
    @ApiModelProperty("执行API")
    private String api;
    @ApiModelProperty("请求方式")
    private String method;
    @ApiModelProperty("header")
    private String header;
    @ApiModelProperty("URL参数")
    private String urlParams;
    @ApiModelProperty("Body")
    private String body;
    private String status;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("规则id")
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

    public String getGroupTaskId() {
        return groupTaskId;
    }

    public void setGroupTaskId(String groupTaskId) {
        this.groupTaskId = groupTaskId;
    }

    public String getGroupTaskName() {
        return groupTaskName;
    }

    public void setGroupTaskName(String groupTaskName) {
        this.groupTaskName = groupTaskName;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDataSourceSourceId() {
        return dataSourceSourceId;
    }

    public void setDataSourceSourceId(String dataSourceSourceId) {
        this.dataSourceSourceId = dataSourceSourceId;
    }

    public String getDataSourceSourceName() {
        return dataSourceSourceName;
    }

    public void setDataSourceSourceName(String dataSourceSourceName) {
        this.dataSourceSourceName = dataSourceSourceName;
    }

    public String getDataSourceTargetId() {
        return dataSourceTargetId;
    }

    public void setDataSourceTargetId(String dataSourceTargetId) {
        this.dataSourceTargetId = dataSourceTargetId;
    }

    public String getDataSourceTargetName() {
        return dataSourceTargetName;
    }

    public void setDataSourceTargetName(String dataSourceTargetName) {
        this.dataSourceTargetName = dataSourceTargetName;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class TasksUpdateVm{
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "任务组id不能为空")
    @ApiModelProperty("任务组id")
    private String groupTaskId;
    @NotBlank(message = "任务组名称不能为空")
    @ApiModelProperty("任务组名称")
    private String groupTaskName;
    @NotBlank(message = "数据来源id不能为空")
    @ApiModelProperty("数据来源id")
    private String dataSourceSourceId;
    @NotBlank(message = "数据来源名称不能为空")
    @ApiModelProperty("数据来源名称")
    private String dataSourceSourceName;
    @NotBlank(message = "目标源id不能为空")
    @ApiModelProperty("目标源id")
    private String dataSourceTargetId;
    @NotBlank(message = "目标源名称不能为空")
    @ApiModelProperty("目标源名称")
    private String dataSourceTargetName;
    @ApiModelProperty("源表")
    private String sourceTable;
    @ApiModelProperty("目标表")
    private String targetTable;
    @ApiModelProperty("增全量策略,增量、全量")
    private String strategy;
    @ApiModelProperty("执行API")
    private String api;
    @ApiModelProperty("请求方式")
    private String method;
    @ApiModelProperty("header")
    private String header;
    @ApiModelProperty("URL参数")
    private String urlParams;
    @ApiModelProperty("Body")
    private String body;
    private String status;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("规则id")
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

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getGroupTaskId() {
        return groupTaskId;
    }

    public void setGroupTaskId(String groupTaskId) {
        this.groupTaskId = groupTaskId;
    }

    public String getGroupTaskName() {
        return groupTaskName;
    }

    public void setGroupTaskName(String groupTaskName) {
        this.groupTaskName = groupTaskName;
    }

    public String getDataSourceSourceId() {
        return dataSourceSourceId;
    }

    public void setDataSourceSourceId(String dataSourceSourceId) {
        this.dataSourceSourceId = dataSourceSourceId;
    }

    public String getDataSourceSourceName() {
        return dataSourceSourceName;
    }

    public void setDataSourceSourceName(String dataSourceSourceName) {
        this.dataSourceSourceName = dataSourceSourceName;
    }

    public String getDataSourceTargetId() {
        return dataSourceTargetId;
    }

    public void setDataSourceTargetId(String dataSourceTargetId) {
        this.dataSourceTargetId = dataSourceTargetId;
    }

    public String getDataSourceTargetName() {
        return dataSourceTargetName;
    }

    public void setDataSourceTargetName(String dataSourceTargetName) {
        this.dataSourceTargetName = dataSourceTargetName;
    }

    public String getSourceTable() {
        return sourceTable;
    }

    public void setSourceTable(String sourceTable) {
        this.sourceTable = sourceTable;
    }

    public String getTargetTable() {
        return targetTable;
    }

    public void setTargetTable(String targetTable) {
        this.targetTable = targetTable;
    }

    public String getStrategy() {
        return strategy;
    }

    public void setStrategy(String strategy) {
        this.strategy = strategy;
    }

    public String getApi() {
        return api;
    }

    public void setApi(String api) {
        this.api = api;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class TasksUpdateStatusVm{
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "状态不能为空,启用或者禁用")
    @ApiModelProperty("状态")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class TaskManualOperationVm{
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

