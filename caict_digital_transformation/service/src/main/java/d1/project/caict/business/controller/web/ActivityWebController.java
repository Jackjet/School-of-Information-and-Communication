package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.Activities;
import d1.project.caict.business.model.ActivityFindAllVm;
import d1.project.caict.business.service.ActivityService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("noauth")
@RestController
@RequestMapping("/web/activities")
public class ActivityWebController {
    private final ActivityService activityService;

    public ActivityWebController(ActivityService activityService) {
        this.activityService = activityService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<Activities>> findAll(@Valid ActivityFindAllVm model) throws Exception {
        return ResultUtil.success("", activityService.findAll(model));
    }

    @GetMapping(value = "/find")
    public Result<Activities> find(@Valid @NotBlank(message = "id不能为空") String id) {
        return ResultUtil.success("", activityService.find(id));
    }

}
