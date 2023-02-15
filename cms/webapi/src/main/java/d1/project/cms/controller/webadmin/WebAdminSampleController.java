package d1.project.cms.controller.webadmin;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.cms.entity.SampleEntity;
import d1.project.cms.service.SampleService;
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
 * 针对管理界面的API接口，token验证
 *
 * @author Buter
 * @date 2020/3/18 17:12
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/sample")
@Api(value = "/webadmin/sample", tags = "示例controller")
public class WebAdminSampleController {
    private final SampleService sampleService;

    @Autowired
    public WebAdminSampleController(SampleService sampleService) {
        this.sampleService = sampleService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<SampleEntity>> findAll(SampleFindVm model) {
        try {
            Page<SampleEntity> result = sampleService.findAll((JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("", result);
        } catch (Exception e) {
            return ResultUtil.fail("登录失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "插入", notes = "插入数据")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(HttpServletRequest request, @Valid @RequestBody SampleInsertUpdateVm model) {
        try {
            this.sampleService.insert((JSONObject) JSONObject.toJSON(model), request);
            return ResultUtil.success("插入成功");
        } catch (Exception e) {
            return ResultUtil.fail("插入失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新", notes = "更新数据")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(HttpServletRequest request, @Valid @RequestBody SampleInsertUpdateVm model) {
        try {
            this.sampleService.update((JSONObject) JSONObject.toJSON(model), request);
            return ResultUtil.success("更新成功");
        } catch (Exception e) {
            return ResultUtil.fail("更新失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除特定数据")
    @RequestMapping(value = "/delete/{id}", method = RequestMethod.DELETE)
    public Result<String> delete(@PathVariable("id") String id) {
        try {
            sampleService.delete(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败：" + e.getMessage(), e);
        }
    }
}

class SampleFindVm extends PageableVm {
    @ApiModelProperty(value = "iField")
    private Integer iField;
    @ApiModelProperty(value = "sField")
    private String sField;
    @ApiModelProperty(value = "开始时间")
    private String stField;
    @ApiModelProperty(value = "结束时间")
    private String etField;

    public Integer getiField() {
        return iField;
    }

    public void setiField(Integer iField) {
        this.iField = iField;
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public String getStField() {
        return stField;
    }

    public void setStField(String stField) {
        this.stField = stField;
    }

    public String getEtField() {
        return etField;
    }

    public void setEtField(String etField) {
        this.etField = etField;
    }
}

class SampleInsertUpdateVm {

    @ApiModelProperty(value = "id")
    private String id;
    @ApiModelProperty(value = "iField")
    private Integer iField;
    @NotBlank(message = "sField不能为空")
    @ApiModelProperty(value = "sField")
    private String sField;
    @ApiModelProperty(value = "tField")
    private String tField;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public Integer getiField() {
        return iField;
    }

    public void setiField(Integer iField) {
        this.iField = iField;
    }

    public String getsField() {
        return sField;
    }

    public void setsField(String sField) {
        this.sField = sField;
    }

    public String gettField() {
        return tField;
    }

    public void settField(String tField) {
        this.tField = tField;
    }
}