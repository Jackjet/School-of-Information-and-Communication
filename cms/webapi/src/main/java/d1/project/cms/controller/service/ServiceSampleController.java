package d1.project.cms.controller.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SignService;
import d1.project.cms.entity.SampleEntity;
import d1.project.cms.service.SampleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * 针对其它开发者提供的服务，appid hmac校验
 *
 * @author Buter
 * @date 2020/3/18 17:12
 */
@RestController
@RequestMapping("/service/sample")
@Api(value = "/service/sample", tags = "示例controller")
public class ServiceSampleController {
    private final SampleService sampleService;
    private final SignService signService;

    @Autowired
    public ServiceSampleController(SampleService sampleService, SignService signService) {
        this.sampleService = sampleService;
        this.signService = signService;
    }

    @ApiOperation(value = "查询", notes = "查询所有数据")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<SampleEntity>> findAll(HttpServletRequest request, @Valid SampleFindVm model) {
        try {
            //先通过HMAC签名验证
            signService.verfiyHmacSign(request, ServiceSampleController.this::getAppKeyById);
            Page<SampleEntity> result = sampleService.findAll((JSONObject) JSONObject.toJSON(model));
            return ResultUtil.success("", result);
        } catch (Exception e) {
            return ResultUtil.fail("登录失败：" + e.getMessage(), e);
        }
    }

    private String getAppKeyById(String appid) {
        //在这里实现通过appid获取appkey的内容
        if (appid.equals("aaaa")) {
            return "bbbb";
        }
        return null;
    }
}

class SampleFindVm  extends PageableVm {
    @NotNull(message = "iField不能为空")
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