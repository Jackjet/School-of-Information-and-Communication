package d1.project.cms.controller.webadmin.column;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.cms.Constants;
import d1.project.cms.business.ColumnBusiness;
import d1.project.cms.entity.column.Column;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import d1.project.cms.entity.template.Template;
import d1.project.cms.model.column.AttachmentVm;
import d1.project.cms.model.column.ColumnResultVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
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
@RequestMapping("/webadmin/column")
@Api(value = "/webadmin/column", tags = "栏目管理")
public class ColumnController {
    private final ColumnBusiness columnBusiness;

    public ColumnController(ColumnBusiness columnBusiness) {
        this.columnBusiness = columnBusiness;
    }

    @ApiOperation(value = "栏目列表", notes = "栏目列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<List<ColumnResultVm>> findAll(ColumnFindAllGetVm params) throws DoValidException {
        List<ColumnResultVm> result = this.columnBusiness.findAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "栏目详情", notes = "栏目详情")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<JSONObject> findById(String id) throws DoValidException {
        JSONObject result = this.columnBusiness.findById(id);
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody ColumnInsertPostVm params) throws DoValidException {
        this.columnBusiness.insert(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("添加成功");
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody ColumnUpdatePutVm params) throws DoValidException {
        this.columnBusiness.update(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("编辑成功");
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> deleteById(String id) throws DoValidException {
        this.columnBusiness.deleteById(id);
        return ResultUtil.success("删除成功");
    }

    @ApiOperation(value = "修改置顶", notes = "修改置顶")
    @RequestMapping(value = "/updatePower", method = RequestMethod.PUT)
    public Result<String> updatePower(@RequestBody ColumnUpdatePowerPutVm params) throws DoValidException {
        this.columnBusiness.updatePower((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("修改成功");
    }

    @ApiOperation(value = "资源库列表", notes = "资源库列表")
    @RequestMapping(value = "/findAllRepository", method = RequestMethod.GET)
    public Result<Page<Repository>> findAllRepository(ColumnFindAllRepositoryGetVm params) throws DoValidException {
        Page<Repository> result = this.columnBusiness.findAllRepository((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "资源库文件列表", notes = "资源库文件列表")
    @RequestMapping(value = "/fileFindAll", method = RequestMethod.GET)
    public Result<Column> fileFindAll(ColumnFileFindAllGetVm params) throws DoValidException {
        Page<ResourceFile> result = this.columnBusiness.fileFindAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "获取模板列表", notes = "获取模板列表")
    @RequestMapping(value = "/templateFindAll", method = RequestMethod.GET)
    public Result<List<Template>> templateFindAll()  throws DoValidException {
        return ResultUtil.success("查询成功", this.columnBusiness.templateFindAll());
    }

    @Auth("noauth")
    @ApiOperation(value = "栏目列表", notes = "栏目列表")
    @RequestMapping(value = "/getAllColumn", method = RequestMethod.GET)
    public Result<List<ColumnResultVm>> getAllColumn() throws DoValidException  {
        JSONObject params = new JSONObject();
        List<ColumnResultVm> result = this.columnBusiness.findAllWithAttachment(params);
        return ResultUtil.success("查询成功", result);
    }
}

class ColumnFindAllGetVm {
    private String columnName;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }
}

class ColumnInsertPostVm {
    @NotBlank(message = "资源库名称不能为空")
    private String columnName;
    @NotBlank(message = "模板不能为空")
    private String templateId;
    private String seoTitle;
    private String seoKeyWord;
    private String parentId;
    private List<AttachmentVm> attachment;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AttachmentVm> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentVm> attachment) {
        this.attachment = attachment;
    }
}

class ColumnUpdatePutVm {
    @NotBlank(message = "栏目id不能为空")
    private String id;
    @NotBlank(message = "资源库名称不能为空")
    private String columnId;
    @NotBlank(message = "资源库名称不能为空")
    private String columnName;
    @NotBlank(message = "资源库名称不能为空")
    private String templateId;
    private String seoTitle;
    private String seoKeyWord;
    private String parentId;
    private List<AttachmentVm> attachment;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public List<AttachmentVm> getAttachment() {
        return attachment;
    }

    public void setAttachment(List<AttachmentVm> attachment) {
        this.attachment = attachment;
    }
}

class ColumnUpdatePowerPutVm {
    @NotBlank(message = "栏目id不能为空")
    private String id;
    @NotBlank(message = "置顶值不能为空")
    private Integer power;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}

class ColumnFindAllRepositoryGetVm extends PageableVm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ColumnFileFindAllGetVm extends PageableVm {
    @NotBlank(message = "资源库id不能为空")
    private String repositoryId;
    private String parentId;

    public String getRepositoryId() {
        return repositoryId;
    }

    public void setRepositoryId(String repositoryId) {
        this.repositoryId = repositoryId;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }
}
