package d1.project.caict.business.controller.webadmin;

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

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/questionnaires")
public class QuestionnaireController {
    private final QuestionnaireService questionnaireService;

    public QuestionnaireController(QuestionnaireService questionnaireService) {
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
}
