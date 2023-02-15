package d1.project.cms.controller.webadmin.template;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.cms.Constants;
import d1.project.cms.business.TemplateBusiness;
import d1.project.cms.entity.template.DataField;
import d1.project.cms.entity.template.Template;
import d1.project.cms.model.template.DataFieldInsertOrUpdateVm;
import d1.project.cms.model.template.TemplateInsertOrUpdateVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * @author baozh
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/template")
@Api(value = "/webadmin/template", tags = "模板")
public class TemplateController {

    private final TemplateBusiness templateBusiness;

    public TemplateController(TemplateBusiness templateBusiness) {
        this.templateBusiness = templateBusiness;
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/find", method = RequestMethod.GET)
    public Result<Page<Template>> find(FindVm findVm) {
        try {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSON(findVm).toString());
            return ResultUtil.success(Constants.SUCCESS,templateBusiness.findTemplate(json));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<Template> findById(String id) {
        try {
            return ResultUtil.success(Constants.SUCCESS,templateBusiness.findTemplateById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody TemplateInsertOrUpdateVm vm, HttpServletRequest request) {
        try {
            templateBusiness.insertTemplate(vm,request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@RequestBody TemplateInsertOrUpdateVm vm, HttpServletRequest request) {
        try {
            templateBusiness.updateTemplate(vm,request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除模板文件", notes = "获取模板文件列表")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(String id) {
        try {
            templateBusiness.deleteTemplate(id);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/findData", method = RequestMethod.GET)
    public Result<Page<DataField>> findData(FindDataVm findVm) {
        try {
            JSONObject json = JSONObject.parseObject(JSONObject.toJSON(findVm).toString());
            return ResultUtil.success(Constants.SUCCESS,templateBusiness.findData(json));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/findDataById", method = RequestMethod.GET)
    public Result<DataField> findDataById(String id) {
        try {
            return ResultUtil.success(Constants.SUCCESS,templateBusiness.findDataById(id));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/insertData", method = RequestMethod.POST)
    public Result<String> insertData(@RequestBody DataFieldInsertOrUpdateVm vm,HttpServletRequest request) {
        try {
            templateBusiness.insertData(vm,request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/updateData", method = RequestMethod.PUT)
    public Result<String> updateData(@RequestBody DataFieldInsertOrUpdateVm vm,HttpServletRequest request) {
        try {
            templateBusiness.updateData(vm,request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取模板文件列表", notes = "获取模板文件列表")
    @RequestMapping(value = "/deleteData", method = RequestMethod.DELETE)
    public Result<String> deleteData(String id) {
        try {
            templateBusiness.deleteData(id);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "上传数据字段JSON文件", notes = "上传数据字段JSON文件")
    @RequestMapping(value = "/uploadJson", method = RequestMethod.POST)
    public Result<String> uploadJson(@RequestParam("file") MultipartFile multipartFile, String templateId, HttpServletRequest request) throws Exception{
        templateBusiness.uploadJson(multipartFile,templateId,request);
        return ResultUtil.success(Constants.SUCCESS);
    }


}

class FindVm extends PageableVm {
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class FindDataVm extends PageableVm {
    private String templateId;
    private String type;

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
