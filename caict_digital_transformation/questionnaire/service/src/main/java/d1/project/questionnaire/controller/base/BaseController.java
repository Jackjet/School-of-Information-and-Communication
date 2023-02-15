package d1.project.questionnaire.controller.base;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.questionnaire.util.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Date;


/**
 * 音频管理
 *
 * @author xuaa
 */
@Api(value = "/base/base", tags = "课程管理 音频管理")
@RestController("/base/base")
@RequestMapping("/base/base")
public class BaseController {


    @ApiOperation(value = "当前时间")
    @GetMapping(value = "/currentTime")
    public Result<String> findAll() {
        try {
            return ResultUtil.success(Constants.SUCCESS, new Date());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

}

