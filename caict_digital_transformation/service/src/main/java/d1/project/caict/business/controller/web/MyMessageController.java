package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.model.MessageContentFindAllByUserIdVm;
import d1.project.caict.business.model.MyMessageContentVm;
import d1.project.caict.business.model.MyMessageReadVm;
import d1.project.caict.business.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

/**
 * 我的活动
 *
 * @author: maorui
 * @date: 2020/9/23
 */

@Auth("web")
@RestController
@RequestMapping("/web/my/message")
public class MyMessageController {
    private final MessageService messageService;

    public MyMessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MyMessageContentVm>> findAll(@Valid MessageContentFindAllByUserIdVm model, HttpServletRequest request) throws Exception {
        return ResultUtil.success("", messageService.findAllByUser(model, request));
    }

    /**
     * 修改消息状态
     */
    @PutMapping(value = "/read")
    public Result read(@Valid @RequestBody MyMessageReadVm model, HttpServletRequest request) throws DoValidException {
        messageService.read(model.getId(), request);
        return ResultUtil.success();
    }

//    @DeleteMapping(value = "/delete")
//    public Result delete(@Valid @NotBlank(message = "id不能为空") String id) {
//        myActivityService.delete(id);
//        return ResultUtil.success();
//    }
}
