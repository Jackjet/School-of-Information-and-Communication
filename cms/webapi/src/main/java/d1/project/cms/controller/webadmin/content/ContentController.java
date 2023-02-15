package d1.project.cms.controller.webadmin.content;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SignService;
import d1.project.cms.business.ContentBusiness;
import d1.project.cms.entity.column.Column;
import d1.project.cms.entity.content.Content;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import d1.project.cms.entity.template.DataField;
import d1.project.cms.model.column.ColumnResultVm;
import d1.project.cms.utils.SingKeyUtils;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.util.FileCopyUtils;
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
@RequestMapping("/webadmin/content")
@Api(value = "/webadmin/content", tags = "内容管理")
public class ContentController {
    private final ContentBusiness contentBusiness;
    private final SignService signService;

    public ContentController(ContentBusiness contentBusiness, SignService signService) {
        this.contentBusiness = contentBusiness;
        this.signService = signService;
    }

    @ApiOperation(value = "内容列表", notes = "内容列表")
    @RequestMapping(value = "/treeFindAll", method = RequestMethod.GET)
    public Result<List<ColumnResultVm>> treeFindAll() throws DoValidException {
        List<ColumnResultVm> result = this.contentBusiness.getColumnTree();
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "内容列表", notes = "内容列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<JSONObject> findAll(ContentFindAllGetVm params) throws DoValidException {
        JSONObject result = this.contentBusiness.findAllByColumnId((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "内容字段", notes = "内容字段")
    @RequestMapping(value = "/fieldFindAll", method = RequestMethod.GET)
    public Result<List<DataField>> fieldFindAll(ContentFieldFindAllGetVm params) throws DoValidException {
        List<DataField> result = this.contentBusiness.fieldFindAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "修改置顶", notes = "修改置顶")
    @RequestMapping(value = "/updatePower", method = RequestMethod.PUT)
    public Result<String> updatePower(HttpServletRequest request, @RequestBody ContentUpdatePowerPutVm params) throws DoValidException {
        this.contentBusiness.updatePower(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("修改成功");
    }

    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody ContentInsertPostVm params) throws DoValidException {
        this.contentBusiness.insert(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("添加成功");
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @RequestBody ContentUpdatePutVm params) throws DoValidException {
        this.contentBusiness.update(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("修改成功");
    }

    @ApiOperation(value = "内容详情", notes = "内容详情")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Content> findById(String id) throws DoValidException {
        Content result = this.contentBusiness.findById(id);
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "复制", notes = "复制")
    @RequestMapping(value = "/copy", method = RequestMethod.POST)
    public Result<String> copy(@RequestBody ContentCopyPostVm params) throws DoValidException {
        this.contentBusiness.copy(params.getId(), params.getColumnId());
        return ResultUtil.success("复制成功");
    }

    @ApiOperation(value = "获取所有栏目", notes = "获取所有栏目")
    @RequestMapping(value = "/findAllColumn", method = RequestMethod.GET)
    public Result<List<Column>> findAllColumn() throws DoValidException {
        List<Column> result = this.contentBusiness.findAllColumn();
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> deleteById(String id) throws DoValidException {
        this.contentBusiness.deleteById(id);
        return ResultUtil.success("删除成功");
    }

    @Auth("noauth")
    @ApiOperation(value = "根据栏目id获取内容", notes = "根据栏目id获取内容")
    @RequestMapping(value = "/getContentByColumnId", method = RequestMethod.GET)
    public Result<JSONObject> getContentByColumnId(ContentGetContentByColumnIdGetVm params) throws DoValidException {
        JSONObject result = this.contentBusiness.getContentByColumnId((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @Auth("noauth")
    @ApiOperation(value = "根据栏目id获取内容", notes = "根据栏目id获取内容")
    @RequestMapping(value = "/getContentByColumnId2", method = RequestMethod.GET)
    public Result<JSONObject> getContentByColumnId2(String columnId) throws DoValidException {
        JSONObject result = this.contentBusiness.getContentByColumnId2(columnId);
        return ResultUtil.success("查询成功", result);
    }

    @Auth("noauth")
    @ApiOperation(value = "根据id获取内容", notes = "根据id获取内容")
    @RequestMapping(value = "/getContentById", method = RequestMethod.GET)
    public Result<JSONObject> getContentById(String id) throws DoValidException {
        JSONObject result = this.contentBusiness.getContentById(id);
        return ResultUtil.success("查询成功", result);
    }

    @Auth("noauth")
    @ApiOperation(value = "根据栏目id获取内容", notes = "根据栏目id获取内容")
    @RequestMapping(value = "/getAllContentByColumnId", method = RequestMethod.GET)
    public Result<JSONObject> getAllContentByColumnId(ContentGetContentByColumnIdGetVm params) throws DoValidException {
        JSONObject result = this.contentBusiness.getAllContentByColumnId((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "资源库列表", notes = "资源库列表")
    @RequestMapping(value = "/findAllRepository", method = RequestMethod.GET)
    public Result<Page<Repository>> findAllRepository(ContentFindAllRepositoryGetVm model) throws DoValidException {
        Page<Repository> result = this.contentBusiness.findAllRepository((JSONObject) JSON.toJSON(model));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "资源库内容列表", notes = "资源库内容列表")
    @RequestMapping(value = "/fileFindAll", method = RequestMethod.GET)
    public Result<Page<ResourceFile>> fileFindAll(ContentFileFindAllRepositoryGetVm params) throws DoValidException {
        Page<ResourceFile> result = this.contentBusiness.fileFindAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功", result);
    }

    @Auth("noauth")
    @ApiOperation(value = "外部接口添加", notes = "外部接口添加")
    @RequestMapping(value = "/addContent", method = RequestMethod.POST)
    public Result<String> addContent(HttpServletRequest request, @RequestBody ContentInsertPostVm params) throws Exception {
        //先通过HMAC签名验证
        signService.verfiyHmacSign(request, SingKeyUtils::getAppKeyById);
        this.contentBusiness.insert(request, (JSONObject) JSON.toJSON(params));
        return ResultUtil.success("添加成功");
    }

    @Auth("noauth")
    @ApiOperation(value = "外部接口获取内容详情", notes = "外部接口获取内容详情")
    @RequestMapping(value = "/callback", method = RequestMethod.GET)
    public Result<String> callback(HttpServletRequest request, String url) throws Exception {
        //先通过HMAC签名验证
        signService.verfiyHmacSign(request, SingKeyUtils::getAppKeyById);
        this.contentBusiness.callback(request, url);
        return ResultUtil.success("设置成功");
    }
}

class ContentFindAllGetVm extends PageableVm {
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


class ContentFieldFindAllGetVm {
    private String templateId;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }
}

class ContentUpdatePowerPutVm {
    @NotBlank(message = "文章id不能为空")
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

class ContentInsertPostVm {
    private String title;
    private String columnId;
    private String content;
    private String type;
    private Integer power;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}

class ContentUpdatePutVm {
    private String id;
    private String title;
    private String content;
    private Integer power;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }
}

class ContentCopyPostVm {
    private String id;
    private String columnId;

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
}

class ContentFindAllRepositoryGetVm extends PageableVm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class ContentFileFindAllRepositoryGetVm extends PageableVm {
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

class ContentGetContentByColumnIdGetVm extends PageableVm {
    private String columnId;

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }
}