package d1.project.cms.controller.webfile;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.annotations.ApiOperation;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.io.File;
import java.text.SimpleDateFormat;

/**
 * WebFileController class
 *
 * @author fengzi
 * @date 2020/9/8
 */
@RestController
@RequestMapping("/webadmin/website")
@Api(value = "/webadmin/website", tags = "网站文件管理")
public class WebFileController {
    private final IFileServerService fileServerService;
    @Value("${d1.framework.webapi.file.service}")
    private String fileDownload;

    public WebFileController(IFileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    @ApiOperation(value = "网站文件上传", notes = "网站文件上传")
    @RequestMapping(value = "/upload", method = RequestMethod.POST)
    public UploadResult webFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("storagePath") String storagePath) throws Exception {
        String path = File.separator + "webFileUpload" + File.separator + storagePath;
        UploadResult result = fileServerService.uploadFile(file, "." + File.separator + storagePath, this.fileDownload + path);
        return result;
    }

    //上传过后 点击取消调用的接口
    @ApiOperation(value = "删除文件", notes = "删除文件")
    @RequestMapping(value = "/deleteFile", method = RequestMethod.GET)
    public Result<JSONArray> deleteFile(@RequestParam("path") String path) {
        try {
            File file = new File(path.substring(path.indexOf("static")));
            file.delete();
            return ResultUtil.success("", "删除成功");
        } catch (Exception e) {
            return ResultUtil.fail("删除失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "新增文件夹", notes = "新增文件夹")
    @RequestMapping(value = "/createDir", method = RequestMethod.POST)
    public Result<String> createDir(HttpServletRequest request, @Valid @RequestBody WebDirVm webDirVm) {
        File file = new File(webDirVm.getDir());
        boolean result = file.mkdirs();
        if (!result) {
            return ResultUtil.fail("新增失败");
        } else {
            return ResultUtil.success("新增成功");
        }
    }

    @ApiOperation(value = "获取某个目录下所有文件夹以及文件", notes = "获取某个目录下所有文件夹以及文件")
    @RequestMapping(value = "/getAllPathByDir", method = RequestMethod.GET)
    public Result<JSONArray> getAllPathByDir(@RequestParam("dir") String dir) {
        try {
            JSONArray jsonArray = new JSONArray();
            getFiles(dir, jsonArray);
            return ResultUtil.success("", jsonArray);
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取当前运行系统", notes = "获取当前运行系统")
    @RequestMapping(value = "/getOS", method = RequestMethod.GET)
    public Result<String> getOS() {
        try {
            return ResultUtil.success("", getSystem());
        } catch (Exception e) {
            return ResultUtil.fail("获取失败：" + e.getMessage(), e);
        }
    }

    /**
     * 返回当前系统 1 Windows  2 Mac OS  3 Linux  4 其他
     *
     * @return 当前系统
     */
    public static int getSystem() {
        String osName = System.getProperty("os.name").toLowerCase();
        System.out.println("current os is " + osName);
        String windowsStr = "windows";
        String linuxStr = "linux";
        String macStr = "mac";
        if (osName.startsWith(windowsStr)) {
            return 1;
        } else if (osName.startsWith(linuxStr)) {
            return 3;
        } else if (osName.startsWith(macStr)) {
            return 2;
        } else {
            return 4;
        }
    }

    public void getFiles(String path, JSONArray list) {
        File file = new File(path);
        if (file.isDirectory()) {
            File[] files = file.listFiles();
            for (File item : files) {
                JSONObject jsonObject = new JSONObject();
                jsonObject.put("path", item.getPath());
                jsonObject.put("fileName", item.getName());
                jsonObject.put("parentPath", item.getParent());
                jsonObject.put("size", FileUtils.sizeOf(item) / 1024);
                jsonObject.put("updateTime", getUpdateTime(item.getPath()));
                if (item.isDirectory()) {
                    jsonObject.put("type", 1);
                } else {
                    jsonObject.put("type", getType(item.getPath()));
                }
                list.add(jsonObject);
            }
        }
    }

    private int getType(String path) {
        String extension = FilenameUtils.getExtension(path);
        if (extension.equals("js")) {
            return 2;
        } else if (extension.equals("css")) {
            return 3;
        } else if (extension.equals("html")) {
            return 4;
        } else if (extension.equals("zip")) {
            return 5;
        } else if (extension.equals("json")) {
            return 6;
        } else {
            return 7;
        }
    }

    private String getUpdateTime(String path) {
        File file = new File(path);
        long modifiedTime = file.lastModified();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return dateFormat.format(modifiedTime);
    }
}

class WebDirVm {
    @ApiModelProperty("目录")
    private String dir;

    public String getDir() {
        return dir;
    }

    public void setDir(String dir) {
        this.dir = dir;
    }
}
