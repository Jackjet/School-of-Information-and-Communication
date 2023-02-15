package d1.project.cms.controller.webadmin.audit;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.cms.business.AuditBusiness;
import d1.project.cms.model.column.ColumnResultVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @author libin
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/audit")
@Api(value = "/webadmin/audit", tags = "内容审批")
public class AuditController {
    private final AuditBusiness auditBusiness;

    public AuditController(AuditBusiness auditBusiness) {
        this.auditBusiness = auditBusiness;
    }

    @ApiOperation(value = "栏目列表", notes = "栏目列表")
    @RequestMapping(value = "/treeFindAll", method = RequestMethod.GET)
    public Result<List<ColumnResultVm>> treeFindAll() throws Exception {
        List<ColumnResultVm> result = this.auditBusiness.getColumnTree();
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "内容列表", notes = "内容列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<JSONObject> findAll(AuditFindAllGetVm params) throws Exception {
        JSONObject result = this.auditBusiness.findAllByColumnId((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "通过", notes = "通过")
    @RequestMapping(value = "/pass", method = RequestMethod.PUT)
    public Result<String> pass(HttpServletRequest request, @RequestBody AuditPassPutVm params) throws DoValidException {
        this.auditBusiness.pass(request,params.getId());
        return ResultUtil.success("操作成功");
    }

    @ApiOperation(value = "驳回", notes = "驳回")
    @RequestMapping(value = "/rejected", method = RequestMethod.PUT)
    public Result<String> rejected(HttpServletRequest request, @RequestBody AuditRejectedPutVm params) throws DoValidException {
        this.auditBusiness.rejected(request,(JSONObject)JSON.toJSON(params));
        return ResultUtil.success("操作成功");
    }
}

class AuditFindAllGetVm extends PageableVm {
    private String columnId;
    private String title;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}

class AuditPassPutVm{
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class AuditRejectedPutVm{
    @NotBlank(message = "id不能为空")
    private String id;
    @NotBlank(message = "驳回原因不能为空")
    private String reasonForRejection;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getReasonForRejection() {
        return reasonForRejection;
    }

    public void setReasonForRejection(String reasonForRejection) {
        this.reasonForRejection = reasonForRejection;
    }
}
