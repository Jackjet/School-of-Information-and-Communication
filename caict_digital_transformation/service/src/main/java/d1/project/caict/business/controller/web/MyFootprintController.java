package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.caict.business.model.MyFootprintFindAllVm;
import d1.project.caict.business.model.MyFootprintInsertVm;
import d1.project.caict.business.service.MyFootprintService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 我的足迹
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/my/footprint")
public class MyFootprintController {
    private final MyFootprintService myFootprintService;

    public MyFootprintController(MyFootprintService myFootprintService) {
        this.myFootprintService = myFootprintService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MyFootprintFindAllVm>> findAll(@Valid PageableVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myFootprintService.findAll(model, request));
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody MyFootprintInsertVm model, HttpServletRequest request) throws Exception {
        myFootprintService.save(model, request);
        return ResultUtil.success();
    }

}
