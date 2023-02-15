package d1.project.caict.bigdata.goverance.controller.webadmin.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.task.GroupTaskService;
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
@RequestMapping("/webadmin/groupTask")
@Api(value = "/webadmin/groupTask", tags = "任务组管理")
public class GroupTaskController {
    private final GroupTaskService groupTaskService;

    @Autowired
    public GroupTaskController(GroupTaskService groupTaskService) {
        this.groupTaskService = groupTaskService;
    }

    @ApiOperation(value = "查询任务组信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<GroupTask>> findAll(GroupTaskGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, groupTaskService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<GroupTask> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            GroupTask groupTask = groupTaskService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, groupTask);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加任务组信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@Valid @RequestBody GroupTaskInsertVm model, HttpServletRequest request) {
        try {
            groupTaskService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新任务组信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody GroupTaskUpdateVm model, HttpServletRequest request) {
        try {
            groupTaskService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除任务组信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            groupTaskService.deleteById(id, request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新任务组状态信息")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
    public Result<String> updateStatus(@Valid @RequestBody GroupTaskUpdateStatusVm model, HttpServletRequest request) {
        try {
            groupTaskService.updateStatus((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.OPERATION_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "手动执行任务组")
    @RequestMapping(value = "/execute", method = RequestMethod.PUT)
    public Result<String> execute(@Valid @RequestBody ManualOperationVm model, HttpServletRequest request) {
        try {
            groupTaskService.execute(model.getId(), request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

class GroupTaskGetVm extends PageableVm {
    @ApiModelProperty("名称")
    private String name;
    @ApiModelProperty("数据来源类型")
    private String sourceCatalog;
    @ApiModelProperty("数据来源id")
    private String dataSourceSourceId;
    @ApiModelProperty("目标源类型")
    private String targetCatalog;
    @ApiModelProperty("目标源id")
    private String dataSourceTargetId;
    @ApiModelProperty("状态")
    private String status;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSourceCatalog() {
        return sourceCatalog;
    }

    public void setSourceCatalog(String sourceCatalog) {
        this.sourceCatalog = sourceCatalog;
    }

    public String getDataSourceSourceId() {
        return dataSourceSourceId;
    }

    public void setDataSourceSourceId(String dataSourceSourceId) {
        this.dataSourceSourceId = dataSourceSourceId;
    }

    public String getTargetCatalog() {
        return targetCatalog;
    }

    public void setTargetCatalog(String targetCatalog) {
        this.targetCatalog = targetCatalog;
    }

    public String getDataSourceTargetId() {
        return dataSourceTargetId;
    }

    public void setDataSourceTargetId(String dataSourceTargetId) {
        this.dataSourceTargetId = dataSourceTargetId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class GroupTaskInsertVm {
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("运行模式,手动模式、周期调度")
    private String operationMode;

    @ApiModelProperty("调度周期")
    private String scheduleCycle;

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

    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("备注")
    private String remark;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getScheduleCycle() {
        return scheduleCycle;
    }

    public void setScheduleCycle(String scheduleCycle) {
        this.scheduleCycle = scheduleCycle;
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

class GroupTaskUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    @ApiModelProperty("运行模式,手动模式、周期调度")
    private String operationMode;

    @ApiModelProperty("调度周期")
    private String scheduleCycle;

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

    @ApiModelProperty("备注")
    private String remark;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOperationMode() {
        return operationMode;
    }

    public void setOperationMode(String operationMode) {
        this.operationMode = operationMode;
    }

    public String getScheduleCycle() {
        return scheduleCycle;
    }

    public void setScheduleCycle(String scheduleCycle) {
        this.scheduleCycle = scheduleCycle;
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

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }
}

class GroupTaskUpdateStatusVm {
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

class ManualOperationVm {
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


