package d1.project.cms.controller.service;

import d1.project.cms.Constants;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author libin
 */
@RestController
@RequestMapping("/service/file")
@Api(value = "/service/file", tags = "文件管理")
public class FileController {
    private final IFileServerService fileServerService;
    @Value("${d1.framework.webapi.file.service}")
    private String fileDownload;

    public FileController(IFileServerService fileServerService) {
        this.fileServerService = fileServerService;
    }

    @ApiOperation(value = "资源库文件上传", notes = "资源库文件上传")
    @RequestMapping(value = "/resourceFile/upload", method = RequestMethod.POST)
    public UploadResult resourceFileUpload(@RequestParam("file") MultipartFile file) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String path = File.separator + "resourceFile" + File.separator + "download" + File.separator + timestamp;
        UploadResult result = fileServerService.uploadFile(file, Constants.RESOURCE_FILE + timestamp, this.fileDownload + path);
        return result;
    }

    @ApiOperation(value = "资源库文件下载", notes = "资源库文件下载")
    @RequestMapping(value = "/resourceFile/download/{timestamp:.+}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> resourceFileDownload(@PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.RESOURCE_FILE + timestamp, fileName);
    }

    @ApiOperation(value = "栏目图片上传", notes = "栏目图片上传")
    @RequestMapping(value = "/column/pictureUpload", method = RequestMethod.POST)
    public UploadResult columnPictureUpload(@RequestParam("file") MultipartFile file) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String path = File.separator + "column" + File.separator + "pictureDownload" + File.separator + timestamp;
        UploadResult result = fileServerService.uploadFile(file, Constants.COLUMN_FILE + timestamp, this.fileDownload + path);
        return result;
    }

    @ApiOperation(value = "栏目图片下载", notes = "栏目图片下载")
    @RequestMapping(value = "/column/pictureDownload/{timestamp:.+}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> columnPictureDownload(@PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.COLUMN_FILE + timestamp, fileName);
    }

    @ApiOperation(value = "内容图片上传", notes = "内容图片上传")
    @RequestMapping(value = "/content/pictureUpload", method = RequestMethod.POST)
    public UploadResult contentPictureUpload(@RequestParam("file") MultipartFile file) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String path = File.separator + "content" + File.separator + "pictureDownload" + File.separator + timestamp;
        UploadResult result = fileServerService.uploadFile(file, Constants.CONTENT_FILE + timestamp, this.fileDownload + path);
        return result;
    }

    @ApiOperation(value = "内容图片下载", notes = "内容图片下载")
    @RequestMapping(value = "/content/pictureDownload/{timestamp:.+}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> contentPictureDownload(@PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.CONTENT_FILE + timestamp, fileName);
    }

    @ApiOperation(value = "内容视频上传", notes = "内容视频上传")
    @RequestMapping(value = "/content/videoUpload", method = RequestMethod.POST)
    public UploadResult contentVideoUpload(@RequestParam("file") MultipartFile file) throws Exception {
        Long timestamp = System.currentTimeMillis();
        String path = File.separator + "content" + File.separator + "videoDownload" + File.separator + timestamp;
        UploadResult result = fileServerService.uploadFile(file, Constants.VIDEO_FILE + timestamp, this.fileDownload + path);
        return result;
    }

    @ApiOperation(value = "内容视频下载", notes = "内容视频下载")
    @RequestMapping(value = "/content/videoDownload/{timestamp:.+}/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> contentVideoDownload(@PathVariable String timestamp, @PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.VIDEO_FILE + timestamp, fileName);
    }
}