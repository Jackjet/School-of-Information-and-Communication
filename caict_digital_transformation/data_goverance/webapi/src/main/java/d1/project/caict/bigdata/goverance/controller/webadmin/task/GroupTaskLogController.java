package d1.project.caict.bigdata.goverance.controller.webadmin.task;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.task.GroupTaskLog;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.task.GroupTaskLogService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/groupTaskLog")
@Api(value = "/webadmin/groupTaskLog", tags = "任务组日志管理")
public class GroupTaskLogController {
    private final GroupTaskLogService groupTaskLogService;

    @Autowired
    public GroupTaskLogController(GroupTaskLogService groupTaskLogService) {
        this.groupTaskLogService = groupTaskLogService;
    }

    @ApiOperation(value = "查询任务组信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<GroupTaskLog>> findAll(GroupTaskLogGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, groupTaskLogService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

class GroupTaskLogGetVm extends PageableVm {
    @ApiModelProperty("名称")
    private String groupTaskName;
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

    public String getGroupTaskName() {
        return groupTaskName;
    }

    public void setGroupTaskName(String groupTaskName) {
        this.groupTaskName = groupTaskName;
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

    public String getDataSourceTargetId() {
        return dataSourceTargetId;
    }

    public void setTargetCatalog(String targetCatalog) {
        this.targetCatalog = targetCatalog;
    }

    public void setDataSourceTargetId(String dataSourceTargetId) {
        this.dataSourceTargetId = dataSourceTargetId;
    }

    public String getTargetCatalog() {
        return targetCatalog;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
