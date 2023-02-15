package d1.project.caict.business.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.Activities;
import d1.project.caict.business.entity.MyActivities;
import d1.project.caict.business.model.ActivityFindAllVm;
import d1.project.caict.business.model.ActivityInsertVm;
import d1.project.caict.business.model.ActivityUpdateVm;
import d1.project.caict.business.model.UsersActivitiesFindAllByActivityIdVm;
import d1.project.caict.business.service.ActivityService;
import d1.project.caict.business.utils.Constants;
import net.dcrun.component.file.server.IFileServerService;
import net.dcrun.component.file.server.UploadResult;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/activities")
public class ActivityController {
    private final ActivityService activityService;
    private final IFileServerService fileServerService;

    public ActivityController(ActivityService activityService, IFileServerService fileServerService) {
        this.activityService = activityService;
        this.fileServerService = fileServerService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<Activities>> findAll(@Valid ActivityFindAllVm model) throws Exception {
        return ResultUtil.success("", activityService.findAll(model));
    }

    @GetMapping(value = "/find")
    public Result<Activities> find(@Valid @NotBlank(message = "id不能为空") String id) {
        return ResultUtil.success("", activityService.find(id));
    }

    @GetMapping(value = "/findUsersByActivityId")
    public Result<Page<MyActivities>> findUsersByActivityId(@Valid UsersActivitiesFindAllByActivityIdVm model) throws Exception {
        return ResultUtil.success("", activityService.findAllUserByActivityId(model));
    }

    @DeleteMapping(value = "/delete")
    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
        activityService.delete(id);
        return ResultUtil.success();
    }

    @PostMapping(value = "/insert")
    public Result insert(@Valid @RequestBody ActivityInsertVm model, HttpServletRequest request) throws Exception {
        activityService.save(model, request);
        return ResultUtil.success();
    }

    @PutMapping(value = "/update")
    public Result insert(@Valid @RequestBody ActivityUpdateVm model, HttpServletRequest request) throws Exception {
        activityService.update(model, request);
        return ResultUtil.success();
    }

    /**
     * 上传活动主图
     *
     * @param file 文件
     * @return 文件上传信息
     * @throws Exception 异常
     */
    @PostMapping(value = "/upload/activities/pic")
    public UploadResult uploadPic(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServerService.uploadFile(file, Constants.ACTIVITIES, "webadmin/activities/download/activities/pic");
    }

    /**
     * 下载活动主图
     *
     * @param fileName 文件名称
     * @param request  request
     * @return 下载流
     * @throws Exception 异常
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/activities/pic/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadPic(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.ACTIVITIES, fileName);
    }

    /**
     * 上传活动附件
     *
     * @param file 文件
     * @return 文件上传信息
     * @throws Exception 异常
     */
    @PostMapping(value = "/upload/activities/attachment")
    public UploadResult uploadAttachment(@RequestParam("file") MultipartFile file) throws Exception {
        return fileServerService.uploadFile(file, Constants.ACTIVITIES, "webadmin/activities/download/activities/attachment");
    }

    /**
     * 下载活动附件
     *
     * @param fileName 文件名称
     * @param request  request
     * @return 下载流
     * @throws Exception 异常
     */
    @Auth("noauth")
    @RequestMapping(value = "/download/activities/attachment/{fileName:.+}", method = RequestMethod.GET)
    public ResponseEntity<Resource> downloadAttachment(@PathVariable String fileName, HttpServletRequest request) throws Exception {
        return fileServerService.downloadFile(request, Constants.ACTIVITIES, fileName);
    }
}
