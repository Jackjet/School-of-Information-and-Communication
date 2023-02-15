package d1.project.questionnaire.controller.webadmin.exam;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.questionnaire.business.ExaminationBusiness;
import d1.project.questionnaire.entity.examination.Examination;
import d1.project.questionnaire.entity.examination.Question;
import d1.project.questionnaire.model.exam.exam.ExamReturnVm;
import d1.project.questionnaire.model.exam.examination.ExaminationInsertVm;
import d1.project.questionnaire.model.exam.examination.ExaminationUpdateVm;
import d1.project.questionnaire.model.exam.question.QuestionFindReturnVm;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.view.entity.ExamGroupNum;
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
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 15:33
 */

@Auth("webadmin")
@Api(value = "/webadmin/examination", tags = "试卷管理")
@RestController("/webadmin/examination")
@RequestMapping("/webadmin/examination")
public class ExaminationController {

    private final ExaminationBusiness examinationBusiness;

    public ExaminationController(ExaminationBusiness examinationBusiness) {
        this.examinationBusiness = examinationBusiness;
    }

    @ApiOperation(value = "查询考试列表")
    @GetMapping(value = "/findAll")
    public Result<Page<Examination>> findAll(ExamFindVm findVm) {
        try {
            JSONObject params = (JSONObject) JSON.toJSON(findVm);
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findExam(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "查询考试详情")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @GetMapping(value = "/findById")
    public Result<ExamReturnVm> findById(String id) {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findExamById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "考试添加", notes = "添加考试")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ExaminationInsertVm insertVm, HttpServletRequest request) {
        try {
            examinationBusiness.insertExamination(insertVm, request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "考试编辑", notes = "编辑考试")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody ExaminationUpdateVm updateVm, HttpServletRequest request) {
        try {
            examinationBusiness.updateExamination(updateVm, request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "考试删除", notes = "删除考试")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id) {
        try {
            examinationBusiness.deleteExamination(id);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "查看题库分组", notes = "查看题库分组")
    @GetMapping(value = "/findGroup")
    public Result<List<ExamGroupNum>> findGroup(GroupFindVm findVm) {
        try {
            JSONObject params = (JSONObject) JSON.toJSON(findVm);
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findAllExamGroup(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "查看试题列表", notes = "查看试题列表")
    @GetMapping(value = "/findQuestion")
    public Result<Page<Question>> findQuestion(ExamQuestionFindVm findVm) {
        try {
            JSONObject params = (JSONObject) JSON.toJSON(findVm);
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findAllExamQuestion(params));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "批量查询试题详情列表", notes = "批量查询试题详情列表")
    @GetMapping(value = "/findQuestionInfo")
    public Result<List<QuestionFindReturnVm>> findQuestionInfo(List<String> ids) {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findQuestionInfo(ids));
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

}

@Data
class ExamFindVm {
    @ApiModelProperty(value = "考试名称")
    private String name;
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "大小")
    private Integer size;
}

@Data
class GroupFindVm {
    @ApiModelProperty(value = "分组名称")
    private String name;
}

@Data
class ExamQuestionFindVm {
    @ApiModelProperty(value = "题目名称")
    private String topic;
    @ApiModelProperty(value = "题目类型")
    private String type;
    @ApiModelProperty(value = "题库分组ID")
    private String examinationGroupId;
    @ApiModelProperty(value = "页数")
    private Integer page;
    @ApiModelProperty(value = "大小")
    private Integer size;
}
