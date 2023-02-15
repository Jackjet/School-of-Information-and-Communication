package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.model.StatisticServiceVisitCountInsertVm;
import d1.project.caict.business.model.StatisticVisitUserInsertVm;
import d1.project.caict.business.service.StatisticService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("noauth")
@RestController
@RequestMapping("/web/statistic")
public class StatisticWebController {
    private final StatisticService statisticService;

    public StatisticWebController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @PostMapping(value = "/insertSystemVisitUser")
    public Result insert(@Valid @RequestBody StatisticVisitUserInsertVm model) throws Exception {
        statisticService.saveSystemVisitUser(model);
        return ResultUtil.success();
    }

    @PostMapping(value = "/insertServiceVisitCount")
    public Result insert(@Valid @RequestBody StatisticServiceVisitCountInsertVm model) throws Exception {
        statisticService.saveStatisticServiceVisitCount(model);
        return ResultUtil.success();
    }
}
