package d1.project.caict.business.controller.web;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.framework.webapi.model.PageableVm;
import net.dcrun.component.http.IHttpService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/resource")
public class ResourceLibraryController {

    private String findAllTableWithDbNameUrl;
    private String findAllDataWithDbNameAndTableNameUrl;

    private final IHttpService httpService;


    public ResourceLibraryController(@Value("${caict.bigdata.goverance.root_url}") String rootUrl, IHttpService httpService) {
        this.httpService = httpService;
        this.findAllTableWithDbNameUrl = rootUrl + "service/resource/findAllTableWithDbName";
        this.findAllDataWithDbNameAndTableNameUrl = rootUrl + "service/resource/findAllDataWithDbNameAndTableName";
    }

    @GetMapping(value = "/findAllTableWithDbName")
    public Result<String> findAllTableWithDbName(@Valid FindAllTableWithDbNameVm model) throws Exception {
        Map<String, Object> params = new HashMap<>((JSONObject) JSONObject.toJSON(model));
        return getResult(httpService.get(findAllTableWithDbNameUrl, null, params));
    }

    @GetMapping(value = "/findAllDataWithDbNameAndTableName")
    public Result<String> findByDbName(@Valid FindAllDataWithDbNameAndTableNameVm model) throws Exception {
        Map<String, Object> params = new HashMap<>((JSONObject) JSONObject.toJSON(model));
        return getResult(httpService.get(findAllDataWithDbNameAndTableNameUrl, null, params));
    }


    private Result<String> getResult(String result) {
        JSONObject resultObj = JSONObject.parseObject(result);
        String code = resultObj.getString("code");
        if ("1".equals(code)) {
            return ResultUtil.success("", resultObj.getJSONObject("data"));
        } else {
            return ResultUtil.fail(resultObj.getString("msg"), resultObj.getJSONObject("data"));
        }
    }
}

class FindAllTableWithDbNameVm extends PageableVm {
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

class FindAllDataWithDbNameAndTableNameVm extends PageableVm {
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
