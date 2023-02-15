package d1.project.cms.controller.webadmin.repository;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.cms.business.RepositoryBusiness;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.NotBlank;

/**
 * @author libin
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/repository")
@Api(value = "/webadmin/repository", tags = "资源库管理")
public class RepositoryController {
    private final RepositoryBusiness repositoryBusiness;

    public RepositoryController(RepositoryBusiness repositoryBusiness) {
        this.repositoryBusiness = repositoryBusiness;
    }

    @ApiOperation(value = "资源库列表", notes = "资源库列表")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<Repository>> findAll(RepositoryFindAllGetVm model) throws DoValidException {
        Page<Repository> result = this.repositoryBusiness.findAll((JSONObject) JSON.toJSON(model));
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "详情", notes = "详情")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Repository> findById(String id) throws DoValidException {
        Repository result = this.repositoryBusiness.findById(id);
        return ResultUtil.success("查询成功", result);
    }

    @ApiOperation(value = "添加", notes = "添加")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @RequestBody RepositoryInsertPostVm model) throws DoValidException {
        this.repositoryBusiness.insert(request, (JSONObject) JSON.toJSON(model));
        return ResultUtil.success("添加成功");
    }

    @ApiOperation(value = "编辑", notes = "编辑")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody RepositoryUpdatePutVm model) throws DoValidException {
        this.repositoryBusiness.update((JSONObject) JSON.toJSON(model));
        return ResultUtil.success("编辑成功");
    }

    @ApiOperation(value = "删除", notes = "删除")
    @RequestMapping(value = "/deleteById", method = RequestMethod.DELETE)
    public Result<String> deleteById(String id) throws DoValidException {
        this.repositoryBusiness.deleteById(id);
        return ResultUtil.success("删除成功");
    }

    @ApiOperation(value = "资源库内容列表", notes = "资源库内容列表")
    @RequestMapping(value = "/fileFindAll", method = RequestMethod.GET)
    public Result<Page<ResourceFile>> fileFindAll(RepositoryFileFindAllGetVm params) throws DoValidException {
        Page<ResourceFile> result = this.repositoryBusiness.fileFindAll((JSONObject) JSON.toJSON(params));
        return ResultUtil.success("查询成功",result);
    }

    @ApiOperation(value = "添加资源文件(或文件夹)", notes = "添加资源文件(或文件夹)")
    @RequestMapping(value = "/fileInsert", method = RequestMethod.POST)
    public Result<String> fileInsert(HttpServletRequest request, @RequestBody RepositoryFileInsertPostVm model) throws DoValidException{
        this.repositoryBusiness.fileInsert(request, (JSONObject) JSON.toJSON(model));
        return ResultUtil.success("添加成功");
    }

    @ApiOperation(value = "删除文件或文件夹", notes = "删除文件或文件夹")
    @RequestMapping(value = "/fileDelete", method = RequestMethod.DELETE)
    public Result<String> fileDelete(String id) throws DoValidException {
        this.repositoryBusiness.fileDelete(id);
        return ResultUtil.success("删除成功");
    }
}

class RepositoryFindAllGetVm extends PageableVm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class RepositoryInsertPostVm{
    @NotBlank(message = "资源库名称不能为空")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class RepositoryUpdatePutVm {
    @NotBlank(message = "资源库id不能为空")
    private String id;
    @NotBlank(message = "资源库名称不能为空")
    private String name;

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
}

class RepositoryFileFindAllGetVm extends PageableVm{
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

class RepositoryFileInsertPostVm{
    @NotBlank(message = "文件或文件夹名不能为空")
    private String fileName;
    private String fileDownloadUri;
    private String fileType;
    private Long size;
    @NotBlank(message = "资源库id不能为空")
    private String repositoryId;
    private String parentId;
    @NotBlank(message = "类型不能为空")
    private String type;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileDownloadUri() {
        return fileDownloadUri;
    }

    public void setFileDownloadUri(String fileDownloadUri) {
        this.fileDownloadUri = fileDownloadUri;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public Long getSize() {
        return size;
    }

    public void setSize(Long size) {
        this.size = size;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
