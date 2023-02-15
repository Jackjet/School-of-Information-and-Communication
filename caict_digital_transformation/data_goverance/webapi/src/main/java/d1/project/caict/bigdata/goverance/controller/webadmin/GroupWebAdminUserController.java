package d1.project.caict.bigdata.goverance.controller.webadmin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.GroupWebAdminUser;
import d1.project.caict.bigdata.goverance.service.GroupWebAdminUserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/groupWebAdminUser")
@Api(value = "/webadmin/groupWebAdminUser", tags = "用户组")
public class GroupWebAdminUserController {

    final
    GroupWebAdminUserService groupWebAdminUserService;

    public GroupWebAdminUserController(GroupWebAdminUserService groupWebAdminUserService) {
        this.groupWebAdminUserService = groupWebAdminUserService;
    }

    @ApiOperation(value = "新增")
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody GroupWebAdminUserControllerInsertVm insert, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(insert);
            groupWebAdminUserService.insert(jsonObject, request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除")
    @DeleteMapping(value = "/delete")
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            groupWebAdminUserService.deleteById(id,request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新")
    @PutMapping(value = "/update")
    public Result<String> update(@Valid @RequestBody GroupWebAdminUserControllerUpdateVm model, HttpServletRequest request) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            groupWebAdminUserService.update(jsonObject, request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询")
    @GetMapping(value = "/findAll")
    public Result<Page<GroupWebAdminUser>> findAll(GroupWebAdminUserControllerFindAllVm model) {
        try {
            JSONObject jsonObject = (JSONObject) JSON.toJSON(model);
            Page<GroupWebAdminUser> all = groupWebAdminUserService.findAll(jsonObject);
            return ResultUtil.success(Constants.SUCCESS, all);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<GroupWebAdminUser> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            GroupWebAdminUser byId = groupWebAdminUserService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, byId);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }
}

@ApiModel(value = "GroupWebAdminUserControllerInsertVm", description = "新增")
class GroupWebAdminUserControllerInsertVm {
    @ApiModelProperty("用户名称")
    @NotBlank(message = "用户名称不可为空")
    private String name;

    @ApiModelProperty("描述")
    private String description;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

@ApiModel(value = "GroupWebAdminUserControllerUpdateVm", description = "更新")
class GroupWebAdminUserControllerUpdateVm {

    @ApiModelProperty("id")
    @NotBlank(message = "id不可为空")
    private String id;

    @ApiModelProperty("描述")
    private String description;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}

@ApiModel(value = "GroupWebAdminUserControllerFindAllVm", description = "查询列表")
class GroupWebAdminUserControllerFindAllVm {

    @ApiModelProperty("用户名称")
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
