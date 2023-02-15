package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.MyCollect;
import d1.project.caict.business.model.MyCollectFindAllVm;
import d1.project.caict.business.model.MyCollectInsertVm;
import d1.project.caict.business.service.MyCollectService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * 我的收藏
 *
 * @author: maorui
 */

@Auth("web")
@RestController
@RequestMapping("/web/my/collect")
public class MyCollectController {
    private final MyCollectService myCollectService;

    public MyCollectController(MyCollectService myCollectService) {
        this.myCollectService = myCollectService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MyCollect>> findAll(@Valid MyCollectFindAllVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myCollectService.findAll(model, request));
    }

    @GetMapping(value = "/find")
    public Result<MyCollect> find(@Valid @NotBlank(message = "cms内容标识不能为空") String cmsContentId, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", myCollectService.find(cmsContentId, request));
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody MyCollectInsertVm model, HttpServletRequest request) throws Exception {
        myCollectService.save(model, request);
        return ResultUtil.success();
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String cmsContentId, HttpServletRequest request) throws DoValidException {
        myCollectService.cancel(cmsContentId, request);
        return ResultUtil.success();
    }
}
