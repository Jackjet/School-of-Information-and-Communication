package d1.project.caict.bigdata.goverance.controller.webadmin.service;

import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.bigdata.goverance.Constants;
import io.swagger.annotations.Api;
import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.server.UploadResult;
import net.dcrun.component.file.util.FileUtilService;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;

/**
 * 文件上传下载类
 *
 * @Author maoyy
 **/
@Api(value = "/webadmin/service/file", tags = "文件存储服务")
@RestController
@RequestMapping("/webadmin/service/file")
public class FileController {

    /**
     * 上传法规与知识文件
     *
     * @param file 文件
     * @return 上传结果
     * @throws Exception 异常
     */
    @RequestMapping(value = "/uploadRegulationKnowledgeFile", method = RequestMethod.POST)
    public Result<UploadResult> uploadBuiltZip(@RequestParam("file") MultipartFile file, String type) throws Exception {
        switch (type) {
            case "政策法规":
                if (new FileUtilService().fileExists(Constants.FILES_REGULATION + file.getOriginalFilename())) {
                    throw new Exception("文件已存在");
                }
                return ResultUtil.success("成功", new FileServerService().uploadFile(file, Constants.FILES_REGULATION, "downloadRegulationKnowledgeFile/"));
            case "知识广场":
                if (new FileUtilService().fileExists(Constants.FILES_KNOWLEDGE + file.getOriginalFilename())) {
                    throw new Exception("文件已存在");
                }

                return ResultUtil.success("成功", new FileServerService().uploadFile(file, Constants.FILES_KNOWLEDGE, "downloadRegulationKnowledgeFile/"));
            default:
                return ResultUtil.fail("类型不正确");
        }

    }

    /**
     * 下载法规与知识文件
     *
     * @param fileName 文件名
     * @param request  下载请求
     * @return 文件
     * @throws Exception 异常
     */
    @RequestMapping(value = "/downloadRegulationKnowledgeFile/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadBuildZip(@PathVariable String fileName, HttpServletRequest request, String type) throws Exception {
        //TODO sign认证
        switch (type) {
            case "政策法规":
                return new FileServerService().downloadFile(request, Constants.FILES_REGULATION, fileName);
            case "知识广场":
                return new FileServerService().downloadFile(request, Constants.FILES_KNOWLEDGE, fileName);
            default:
                throw new Exception("类型不正确");
        }
    }
}
