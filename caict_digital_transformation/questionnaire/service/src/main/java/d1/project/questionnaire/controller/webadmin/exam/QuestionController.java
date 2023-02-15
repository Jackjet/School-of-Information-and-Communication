package d1.project.questionnaire.controller.webadmin.exam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.questionnaire.business.ExaminationBusiness;
import d1.project.questionnaire.entity.examination.ExaminationGroup;
import d1.project.questionnaire.model.exam.question.MoveQuestion;
import d1.project.questionnaire.model.exam.question.QuestionFindReturnVm;
import d1.project.questionnaire.model.exam.question.QuestionInsertVm;
import d1.project.questionnaire.model.exam.question.QuestionUpdateVm;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.view.entity.QuestionBank;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import lombok.Data;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;


/**
 * @author baozh
 */
@Auth(value = "webadmin")
@Api(value = "/webadmin/question", tags = "题库")
@RestController("/webadmin/question")
@RequestMapping("/webadmin/question")
public class QuestionController {
    private final ExaminationBusiness examinationBusiness;

    public QuestionController(ExaminationBusiness examinationBusiness) {
        this.examinationBusiness = examinationBusiness;
    }

    @ApiOperation(value = "获取试题列表")
    @GetMapping(value = "/findAll")
    public Result<Page<QuestionBank>> findAll(QuestionFindVm findVm) {
        try {
            JSONObject params = (JSONObject) JSON.toJSON(findVm);
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findQuestionList(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "获取题库分组列表")
    @GetMapping(value = "/findGroup")
    public Result<List<ExaminationGroup>> findGroup() {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.getExaminationGroup());
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "获取试题详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @GetMapping(value = "/findById")
    public Result<QuestionFindReturnVm> findById(String id) {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findQuestionById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "添加试题")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody QuestionInsertVm insertVm, HttpServletRequest request) {
        try {
            examinationBusiness.insertQuestion(insertVm, request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "修改试题")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody QuestionUpdateVm updateVm, HttpServletRequest request) {
        try {
            examinationBusiness.updateQuestion(updateVm, request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "批量删除试题")
    @PostMapping(value = "/deleteByIds")
    public Result<String> deleteByIds(@RequestBody List<String> ids) {
        try {
            examinationBusiness.deleteQuestions(ids);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "移动试题分组")
    @PostMapping(value = "/moveByIds")
    public Result<String> moveByIds(@RequestBody MoveQuestion moveVm) {
        try {
            examinationBusiness.moveQuestions(moveVm);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

}

@Data
class QuestionFindVm {
    @ApiModelProperty(value = "类型(0单选1多选)")
    private String type;
    @ApiModelProperty(value = "题目")
    private String topic;
    @ApiModelProperty(value = "考试分组id")
    private String examinationGroupId;
    @ApiModelProperty(value = "页码")
    private Integer page;
    @ApiModelProperty(value = "大小")
    private Integer size;
}
