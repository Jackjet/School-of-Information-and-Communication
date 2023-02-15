package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.caict.business.entity.MyActivities;
import d1.project.caict.business.model.MyActivityInsertVm;
import d1.project.caict.business.model.MyActivityResultVm;
import d1.project.caict.business.service.MyActivityService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 我的活动
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/my/activity")
public class MyActivityController {
    private final MyActivityService myActivityService;

    public MyActivityController(MyActivityService myActivityService) {
        this.myActivityService = myActivityService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MyActivityResultVm>> findAll(@Valid PageableVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myActivityService.findAll(model, request));
    }

    @GetMapping(value = "/find")
    public Result<MyActivities> find(@Valid @NotBlank(message = "活动id不能为空") String activityId, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myActivityService.find(activityId, request));
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody MyActivityInsertVm model, HttpServletRequest request) throws Exception {
        myActivityService.save(model, request);
        return ResultUtil.success();
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        myActivityService.delete(id);
        return ResultUtil.success();
    }
}
