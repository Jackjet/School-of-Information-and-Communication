package d1.project.caict.business.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.model.StatisticFindVm;
import d1.project.caict.business.model.StatisticServiceVisitCountInsertVm;
import d1.project.caict.business.model.StatisticVisitUserInsertVm;
import d1.project.caict.business.service.StatisticService;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/statistic")
public class StatisticController {
    private final StatisticService statisticService;

    public StatisticController(StatisticService statisticService) {
        this.statisticService = statisticService;
    }

    @GetMapping(value = "/findActiveUser")
    public Result<List<Map<String, String>>> statisticActiveUser(@Valid StatisticFindVm model) {
        return ResultUtil.success("", statisticService.statisticActiveUser(model.getStartTime(), model.getEndTime()));
    }

    @GetMapping(value = "/findVisitUser")
    public Result<List<Map<String, String>>> statisticSystemVisitUser(@Valid StatisticFindVm model) {
        return ResultUtil.success("", statisticService.statisticSystemVisitUser(model.getStartTime(), model.getEndTime()));
    }

    @GetMapping(value = "/findServiceVisitCount")
    public Result<List<Map<String, String>>> statisticServiceVisitCount(@Valid StatisticFindVm model) {
        return ResultUtil.success("", statisticService.statisticServiceVisitCount(model.getStartTime(), model.getEndTime()));
    }

    @GetMapping(value = "/findNewUser")
    public Result<List<Map<String, String>>> statisticNewUser(@Valid StatisticFindVm model) {
        return ResultUtil.success("", statisticService.statisticNewUser(model.getStartTime(), model.getEndTime()));
    }

    @GetMapping(value = "/findNewNews")
    public Result<Map<String, Object>> statisticNewNews(@Valid StatisticFindVm model) {
        return ResultUtil.success("", statisticService.statisticNewNews(model.getStartTime(), model.getEndTime()));
    }

    @GetMapping(value = "/findUserSource")
    public Result<Map<String, Object>> statisticUserSource() {
        return ResultUtil.success("", statisticService.statisticUserSource());
    }

    @GetMapping(value = "/findUserAddress")
    public Result<Map<String, Object>> statisticUserAddress() {
        return ResultUtil.success("", statisticService.statisticUserAddress());
    }
}
