package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.Industries;
import d1.project.caict.business.model.IndustryFindAllVm;
import d1.project.caict.business.service.IndustryService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/industries")
public class IndustryWebController {
    private final IndustryService industryService;

    public IndustryWebController(IndustryService industryService) {
        this.industryService = industryService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<Industries>> findAll(@Valid IndustryFindAllVm model) throws Exception {
        return ResultUtil.success("", industryService.findAll(model));
    }
}
