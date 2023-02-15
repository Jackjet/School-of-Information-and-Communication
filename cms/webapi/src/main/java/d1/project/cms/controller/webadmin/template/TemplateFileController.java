package d1.project.cms.controller.webadmin.template;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.Constants;
import d1.project.cms.entity.homepage.QuickNavigation;
import d1.project.cms.model.template.FileContent;
import d1.project.cms.model.template.FileList;
import d1.project.cms.model.template.InsertFile;
import d1.project.cms.service.template.TemplateFileService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

/**
 * @author baozh
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/templateFile")
@Api(value = "/webadmin/templateFile", tags = "模板文件")
public class TemplateFileController {
    private final TemplateFileService templateFileService;

    public TemplateFileController(TemplateFileService templateFileService) {
        this.templateFileService = templateFileService;
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Result<List<FileList>> find(String parentUrl) {
        try {
            return ResultUtil.success(Constants.SUCCESS,templateFileService.getFiles(parentUrl));
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "文件内容", notes = "获取模板文件列表")
    @RequestMapping(value = "/content", method = RequestMethod.GET)
    public Result<FileContent> content(String url,String name) {
        try {
            return ResultUtil.success(Constants.SUCCESS,templateFileService.getContent(url,name));
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加/编辑--文件", notes = "添加文件")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insertOrUpdate(@RequestBody InsertFile insertFile) {
        try {
            templateFileService.insertOrUpdateFile(insertFile);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加/编辑--文件", notes = "添加文件")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody InsertFile insertFile) {
        try {
            templateFileService.insertOrUpdateFile(insertFile);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "上传文件", notes = "上传文件")
    @RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
    public Result<String> uploadFile(@RequestParam("file") MultipartFile[] list , String url) {
        try {
            templateFileService.uploadFile(list,url);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }
}
