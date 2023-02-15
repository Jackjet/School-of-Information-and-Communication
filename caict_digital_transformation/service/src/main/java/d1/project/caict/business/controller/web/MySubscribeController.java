package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.MyCollect;
import d1.project.caict.business.entity.MySubscribe;
import d1.project.caict.business.model.MySubscribeFindAllVm;
import d1.project.caict.business.model.MySubscribeInsertVm;
import d1.project.caict.business.service.MySubscribeService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 我的订阅
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/my/subscribe")
public class MySubscribeController {
    private final MySubscribeService mySubscribeService;

    public MySubscribeController(MySubscribeService mySubscribeService) {
        this.mySubscribeService = mySubscribeService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MySubscribe>> findAll(@Valid MySubscribeFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", mySubscribeService.findAll(model,request));
    }

    @GetMapping(value = "/find")
    public Result<MyCollect> find(@Valid @NotBlank(message = "cms内容标识不能为空") String cmsContentId, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", mySubscribeService.find(cmsContentId, request));
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody MySubscribeInsertVm model, HttpServletRequest request) throws Exception {
        mySubscribeService.save(model, request);
        return ResultUtil.success();
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        mySubscribeService.delete(id);
        return ResultUtil.success();
    }
}
