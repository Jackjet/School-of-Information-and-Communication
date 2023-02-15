package d1.project.caict.bigdata.goverance.controller.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.model.PageableVm;
import d1.project.caict.bigdata.goverance.service.ResourceLibraryService;
import d1.project.caict.bigdata.goverance.service.datasource.JobDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@RestController
@RequestMapping("/service/resource")
@Api(value = "/service/resource", tags = "资源库查询")
public class ResourceLibraryController {
    private final ResourceLibraryService resourceLibraryService;

    @Autowired
    public ResourceLibraryController(ResourceLibraryService resourceLibraryService) {
        this.resourceLibraryService = resourceLibraryService;
    }

    @GetMapping(value = "/findAllTableWithDbName")
    public Result<JobDataSource> findAllTableWithDbName(@Valid MetaDataTableGetAllWithDbNameVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, resourceLibraryService.findAllTableWithDbName((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @GetMapping(value = "/findAllDataWithDbNameAndTableName")
    public Result<String> findAllDataWithDbNameAndTableName(@Valid FindAllDataWithDbNameAndTableNameVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, resourceLibraryService.findAllDataWithDbNameAndTableName((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

class MetaDataTableGetAllWithDbNameVm extends PageableVm {
    @NotBlank(message = "资源库名称不可为空")
    @ApiModelProperty("数据库名称")
    private String dbName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }
}

class FindAllDataWithDbNameAndTableNameVm extends d1.framework.webapi.model.PageableVm {
    @NotBlank(message = "资源库名称不可为空")
    @ApiModelProperty("数据库名称")
    private String dbName;

    @NotBlank(message = "资源库表名称不可为空")
    @ApiModelProperty("表名称")
    private String tableName;

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }
}

