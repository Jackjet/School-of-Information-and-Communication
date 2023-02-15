package d1.project.caict.business.controller.web;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.es.News;
import d1.project.caict.business.service.CallbackService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@RestController
@RequestMapping("/web/callback")
public class CallbackController {
    private final CallbackService callbackService;

    public CallbackController(CallbackService callbackService) {
        this.callbackService = callbackService;
    }

    @PostMapping(value = "/cms")
    public Result cmsCallback(@RequestBody List<News> news, HttpServletRequest request) throws Exception {
        callbackService.cmsCallback(news, request);
        return ResultUtil.success();
    }
}
