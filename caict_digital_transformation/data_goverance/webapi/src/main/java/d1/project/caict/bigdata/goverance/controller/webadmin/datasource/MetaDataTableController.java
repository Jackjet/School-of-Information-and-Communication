package d1.project.caict.bigdata.goverance.controller.webadmin.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.datasource.MetaDataTableService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/metaDataTable")
@Api(value = "/webadmin/metaDataTable", tags = "数据源管理")
public class MetaDataTableController {

    private final MetaDataTableService metaDataTableService;

    @Autowired
    public MetaDataTableController(MetaDataTableService metaDataTableService) {
        this.metaDataTableService = metaDataTableService;
    }

    @ApiOperation(value = "查询数据源元数据信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<MetaDataTable> findAll(MetaDataTableGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, metaDataTableService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询数据源元数据字段信息")
    @RequestMapping(value = "/findById", method = RequestMethod.GET)
    public Result<String> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            MetaDataTable metaDataTable = metaDataTableService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, metaDataTable.getTableFields());
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询数据源元数据字段信息")
    @RequestMapping(value = "/findAllTableData", method = RequestMethod.GET)
    public Result<String> findAllTableData(@Valid MetaDataTableFindAllTableDataVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, metaDataTableService.findAllTableData(model.getId(), model.getPage(), model.getSize()));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据源元数据信息")
    @RequestMapping(value = "/update", method = RequestMethod.POST)
    public Result<String> update(@Valid @RequestBody MetaDataTableUpdateVm model, HttpServletRequest request) {
        try {
            metaDataTableService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.OPERATION_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail("请求超时或服务器异常，请检查连接配置或联系管理员！", e);
        }
    }

    @ApiOperation(value = "更新数据源元数据字段信息")
    @RequestMapping(value = "/updateFields", method = RequestMethod.POST)
    public Result<String> updateFields(@Valid @RequestBody MetaDataTableUpdateFiledsVm model, HttpServletRequest request) {
        try {
            metaDataTableService.updateFields((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.OPERATION_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail("请求超时或服务器异常，请检查连接配置或联系管理员！", e);
        }
    }
}

class MetaDataTableGetVm extends PageableVm {
    @NotBlank(message = "数据源id不能为空")
    @ApiModelProperty("数据源id")
    private String dataSourceId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}

class MetaDataTableUpdateVm {
    @NotBlank(message = "数据源id不能为空")
    @ApiModelProperty("数据源id")
    private String dataSourceId;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }
}

class MetaDataTableUpdateFiledsVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

class MetaDataTableFindAllTableDataVm extends PageableVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}







