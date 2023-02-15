package d1.project.caict.bigdata.goverance.controller.webadmin.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.service.datasource.JobDataSourceService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author maoyy
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/dataSource")
@Api(value = "/webadmin/dataSource", tags = "数据源管理")
public class JobDataSourceController {
    private final JobDataSourceService jobDataSourceService;

    @Autowired
    public JobDataSourceController(JobDataSourceService jobDataSourceService) {
        this.jobDataSourceService = jobDataSourceService;
    }

    @ApiOperation(value = "查询数据源信息")
    @RequestMapping(value = "/findAll", method = RequestMethod.GET)
    public Result<Page<JobDataSource>> findAll(JobDataSourceGetVm model) {
        try {
            return ResultUtil.success(Constants.SUCCESS, jobDataSourceService.findAll((JSONObject) JSON.toJSON(model)));
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "详情")
    @GetMapping(value = "/findById")
    public Result<JobDataSource> findById(@Valid @NotBlank(message = "id不可为空") String id) {
        try {
            JobDataSource jobDataSource = jobDataSourceService.findById(id).orElseThrow(() -> new Exception("找不到记录:" + id));
            return ResultUtil.success(Constants.SUCCESS, jobDataSource);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加数据源信息")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@Valid @RequestBody JobDataSourceInsertVm model, HttpServletRequest request) {
        try {
            jobDataSourceService.insert((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.ADD_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据源信息")
    @RequestMapping(value = "/update", method = RequestMethod.PUT)
    public Result<String> update(@Valid @RequestBody JobDataSourceUpdateVm model, HttpServletRequest request) {
        try {
            jobDataSourceService.update((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.UPDATE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除数据源信息")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(@Valid @NotBlank(message = "id不可为空") String id, HttpServletRequest request) {
        try {
            jobDataSourceService.deleteById(id, request);
            return ResultUtil.success(Constants.DELETE_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "更新数据源状态信息")
    @RequestMapping(value = "/updateStatus", method = RequestMethod.PUT)
    public Result<String> updateStatus(@Valid @RequestBody JobDataSourceUpdateStatusVm model, HttpServletRequest request) {
        try {
            jobDataSourceService.updateStatus((JSONObject) JSON.toJSON(model), request);
            return ResultUtil.success(Constants.OPERATION_SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

    @ApiOperation(value = "连接数据源")
    @RequestMapping(value = "/connect", method = RequestMethod.PUT)
    public Result<Boolean> connect(@Valid @RequestBody JobDataSource model) {
        try {
            jobDataSourceService.dataSourceTest(model);
            return ResultUtil.success("连接成功");
        } catch (Exception e) {
            return ResultUtil.fail(e.getMessage(), e);
        }
    }

}

class JobDataSourceGetVm extends PageableVm {
    @ApiModelProperty("数据源状态")
    private String status;
    @ApiModelProperty("数据源分类")
    private String catalog;
    @ApiModelProperty("数据源类型")
    private String type;
    @ApiModelProperty("名称")
    private String name;

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}

class JobDataSourceInsertVm {
    @NotBlank(message = "名称不能为空")
    @ApiModelProperty("名称")
    private String name;

    @NotBlank(message = "数据源分类不能为空")
    @ApiModelProperty("数据源分类")
    private String catalog;

    @NotBlank(message = "数据源类型不能为空")
    @ApiModelProperty("数据源类型")
    private String type;

    @ApiModelProperty("数据源地址")
    private String ipAddress;

    @NotBlank(message = "数据源用户名不能为空")
    @ApiModelProperty("用户")
    private String userName;

    @NotBlank(message = "数据源密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "数据源jdbc连接字符串不能为空")
    @ApiModelProperty("jdbc url")
    private String jdbcUrl;

    @NotBlank(message = "数据源jdbc驱动不能为空")
    @ApiModelProperty("jdbc驱动")
    private String jdbcServer;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("zk地址")
    private String zkAddress;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("目录")
    private String folder;

    @ApiModelProperty("header")
    private String header;

    @ApiModelProperty("URL参数")
    private String urlParams;

    @ApiModelProperty("body")
    private String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcServer() {
        return jdbcServer;
    }

    public void setJdbcServer(String jdbcServer) {
        this.jdbcServer = jdbcServer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

class JobDataSourceUpdateVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;

    @NotBlank(message = "数据源分类不能为空")
    @ApiModelProperty("数据源分类")
    private String catalog;

    @NotBlank(message = "数据源类型不能为空")
    @ApiModelProperty("数据源类型")
    private String type;

    @ApiModelProperty("数据源地址")
    private String ipAddress;

    @NotBlank(message = "数据源用户名不能为空")
    @ApiModelProperty("用户")
    private String userName;

    @NotBlank(message = "数据源密码不能为空")
    @ApiModelProperty("密码")
    private String password;

    @NotBlank(message = "数据源jdbc连接字符串不能为空")
    @ApiModelProperty("jdbc url")
    private String jdbcUrl;

    @NotBlank(message = "数据源jdbc驱动不能为空")
    @ApiModelProperty("jdbc驱动")
    private String jdbcServer;

    @ApiModelProperty("备注")
    private String remark;

    @ApiModelProperty("zk地址")
    private String zkAddress;

    @ApiModelProperty("地址")
    private String address;

    @ApiModelProperty("数据库名称")
    private String dbName;

    @ApiModelProperty("目录")
    private String folder;

    @ApiModelProperty("header")
    private String header;

    @ApiModelProperty("URL参数")
    private String urlParams;

    @ApiModelProperty("body")
    private String body;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcServer() {
        return jdbcServer;
    }

    public void setJdbcServer(String jdbcServer) {
        this.jdbcServer = jdbcServer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

class JobDataSourceUpdateStatusVm {
    @NotBlank(message = "id不能为空")
    @ApiModelProperty("id")
    private String id;
    @NotBlank(message = "状态不能为空,启用或者禁用")
    @ApiModelProperty("状态")
    private String status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}

class JobDataSourceConnectVm {
    @NotBlank(message = "数据源类型不能为空")
    @ApiModelProperty("数据源类型")
    private String type;
    @ApiModelProperty("数据源地址")
    private String ipAddress;
    @ApiModelProperty("状态")
    private String status;
    @ApiModelProperty("用户")
    private String userName;
    @ApiModelProperty("密码")
    private String password;
    @ApiModelProperty("jdbc url")
    private String jdbcUrl;
    @ApiModelProperty("jdbc驱动")
    private String jdbcServer;
    @ApiModelProperty("备注")
    private String remark;
    @ApiModelProperty("zk地址")
    private String zkAddress;
    @ApiModelProperty("地址")
    private String address;
    @ApiModelProperty("数据库名称")
    private String dbName;
    @ApiModelProperty("目录")
    private String folder;
    @ApiModelProperty("header")
    private String header;
    @ApiModelProperty("URL参数")
    private String urlParams;
    @ApiModelProperty("body")
    private String body;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcServer() {
        return jdbcServer;
    }

    public void setJdbcServer(String jdbcServer) {
        this.jdbcServer = jdbcServer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

