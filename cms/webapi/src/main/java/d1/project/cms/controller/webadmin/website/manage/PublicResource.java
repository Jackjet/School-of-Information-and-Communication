package d1.project.cms.controller.webadmin.website.manage;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.dcrun.component.file.util.FileUtilService;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * PublicResource class
 *
 * @author fengzi
 * @date 2020/9/10
 */
@RestController
@RequestMapping("/webadmin/publicResource")
@Api(value = "/webadmin/publicResource", tags = "公共资源管理")
public class PublicResource {
    private final FileUtilService fileUtilService;

    public PublicResource(FileUtilService fileUtilService) {
        this.fileUtilService = fileUtilService;
    }

    @ApiOperation(value = "获取所有公共资源", notes = "获取所有公共资源")
    @RequestMapping(value = "/getAllPublicResource", method = RequestMethod.GET)
    public Result<JSONArray> getAllPublicResource() {
        try {
            File file = new File(Constants.PUBLICRESOURCE_FILE);
            JSONArray jsonArray;
            if (!file.exists()) {
                jsonArray = new JSONArray();
                insertData(jsonArray);
            } else {
                String result = fileUtilService.readFileToString(Constants.PUBLICRESOURCE_FILE, "utf-8");
                jsonArray = JSONArray.parseArray(result);
                if (jsonArray.size() == 0) {
                    insertData(jsonArray);
                }
            }
            return ResultUtil.success("", jsonArray);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "保存公共资源", notes = "保存公共资源")
    @RequestMapping(value = "/saveData", method = RequestMethod.POST)
    public Result<String> saveData(HttpServletRequest request, @Valid @RequestBody PublicResourceVm publicResourceVm) {
        try {
            JSONArray jsonArray = publicResourceVm.getResourceData();
            String theKey = check(jsonArray);
            if (theKey.length() == 0) {
                File file = new File(Constants.PUBLICRESOURCE_FILE);
                if (!file.exists()) {
                    if (!file.getParentFile().exists()) {
                        file.getParentFile().mkdirs();
                    }
                    file.createNewFile();
                }
                fileUtilService.writeStringToFile(Constants.PUBLICRESOURCE_FILE, jsonArray.toString());
                return ResultUtil.success("保存成功");
            } else {
                return ResultUtil.fail("key为" + theKey + " 重复,请重新输入");
            }
        } catch (Exception e) {
            return ResultUtil.fail("保存失败：" + e.getMessage(), e);
        }
    }

    private String check(JSONArray jsonArray) {
        String value = "";
        List<String> temp = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JSONObject job = jsonArray.getJSONObject(i);
            if (temp.contains(job.getString("theKey"))) {
                value = job.getString("theKey");
                break;
            } else {
                temp.add(job.getString("theKey"));
            }
        }
        return value;
    }

    private void insertData(JSONArray jsonArray) {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("theKey", "");
        jsonObject.put("theValue", "");
        jsonArray.add(jsonObject);
    }
}

class PublicResourceVm {
    @ApiModelProperty("resourceData")
    private JSONArray resourceData;

    public JSONArray getResourceData() {
        return resourceData;
    }

    public void setResourceData(JSONArray resourceData) {
        this.resourceData = resourceData;
    }
}
