package d1.project.questionnaire.controller.webadmin.exam;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.questionnaire.business.ExaminationBusiness;
import d1.project.questionnaire.entity.examination.ExaminationGroup;
import d1.project.questionnaire.model.exam.examgroup.ExamGroupInsertVm;
import d1.project.questionnaire.model.exam.examgroup.ExamGroupUpdateVm;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.view.entity.ExamGroupNum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.List;

/**
 * @author baozh
 */
@Auth(value = "webadmin")
@Api(value = "/webadmin/examinationGroup", tags = "题库分组")
@RestController("/webadmin/examinationGroup")
@RequestMapping("/webadmin/examinationGroup")
public class ExaminationGroupController {
    private final ExaminationBusiness examinationBusiness;

    public ExaminationGroupController(ExaminationBusiness examinationBusiness) {
        this.examinationBusiness = examinationBusiness;
    }

    @ApiOperation(value = "添加", notes = "添加题库分组")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody ExamGroupInsertVm insertVm, HttpServletRequest request) {
        try {
            examinationBusiness.insertExamGroup(insertVm, request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "查看题库", notes = "查看题库")
    @ApiImplicitParam(name = "id", value = "id", required = true, dataType = "String")
    @GetMapping(value = "/findById")
    public Result<ExaminationGroup> findById(String id) {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findExamGroupById(id));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "修改", notes = "修改题库分组")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody ExamGroupUpdateVm updateVm) {
        try {
            examinationBusiness.updateExamGroup(updateVm);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }

    @ApiOperation(value = "删除", notes = "删除(存在关联内容不能删除)")
    @ApiImplicitParam(name = "id", value = "实体ID", required = true, dataType = "String")
    @DeleteMapping(value = "/deleteById")
    public Result<String> deleteById(String id) {
        try {
            examinationBusiness.deleteExamGroupById(id);
            return ResultUtil.success("删除成功");
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(e.getMessage());
        }
    }

    @ApiOperation(value = "查看题库", notes = "查看题库")
    @GetMapping(value = "/findAll")
    public Result<List<ExamGroupNum>> findAll() {
        try {
            return ResultUtil.success(Constants.SUCCESS, examinationBusiness.findAllExamGroup(new JSONObject()));
        } catch (Exception e) {
            e.printStackTrace();
            return ResultUtil.fail(Constants.FAIL, e.getMessage());
        }
    }
}
