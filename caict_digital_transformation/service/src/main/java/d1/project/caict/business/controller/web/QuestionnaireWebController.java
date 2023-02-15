package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.Questionnaires;
import d1.project.caict.business.model.QuestionnaireFindAllVm;
import d1.project.caict.business.model.QuestionnaireInsertVm;
import d1.project.caict.business.model.QuestionnaireUpdateVm;
import d1.project.caict.business.service.QuestionnaireService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/questionnaires")
public class QuestionnaireWebController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireWebController(QuestionnaireService questionnaireService) {
        this.questionnaireService = questionnaireService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<Questionnaires>> findAll(@Valid QuestionnaireFindAllVm model) throws Exception {
        return ResultUtil.success("", questionnaireService.findAll(model));
    }

    @GetMapping(value = "/find")
    public Result<Questionnaires> find(@Valid @NotBlank(message = "id不能为空") String id) {
        return ResultUtil.success("", questionnaireService.find(id));
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        questionnaireService.delete(id);
        return ResultUtil.success();
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody QuestionnaireInsertVm model, HttpServletRequest request) throws Exception {
        questionnaireService.save(model, request);
        return ResultUtil.success();
    }

    @PutMapping(value = "/update")
    public Result update(@Valid @RequestBody QuestionnaireUpdateVm model, HttpServletRequest request) throws Exception {
        questionnaireService.update(model, request);
        return ResultUtil.success();
    }

}
