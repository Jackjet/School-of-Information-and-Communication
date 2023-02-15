package d1.project.caict.business.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.Industries;
import d1.project.caict.business.model.IndustryFindAllVm;
import d1.project.caict.business.model.IndustryInsertVm;
import d1.project.caict.business.model.IndustryUpdateVm;
import d1.project.caict.business.service.IndustryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/industries")
public class IndustryController {
    private final IndustryService industryService;

    public IndustryController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<Industries>> findAll(@Valid IndustryFindAllVm model) throws Exception {
        return ResultUtil.success("", industryService.findAll(model));
    }

    @Auth("noauth")
    @GetMapping(value = "/findAllNoPage")
    public Result findAllNoPage() {
        Map<String, String> result = industryService.findAllNoPage();
        return ResultUtil.success("", result);
    }

    @GetMapping(value = "/find")
    public Result<Industries> find(@Valid @NotBlank(message = "id不能为空") String id) {
        return ResultUtil.success("", industryService.find(id));
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        industryService.delete(id);
        return ResultUtil.success();
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody IndustryInsertVm model, HttpServletRequest request) throws Exception {
        industryService.save(model, request);
        return ResultUtil.success();
    }

    @PutMapping(value = "/update")
    public Result update(@Valid @RequestBody IndustryUpdateVm model, HttpServletRequest request) throws Exception {
        industryService.update(model, request);
        return ResultUtil.success();
    }
}
