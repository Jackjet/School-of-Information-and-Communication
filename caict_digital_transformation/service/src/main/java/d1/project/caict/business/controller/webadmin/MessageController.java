package d1.project.caict.business.controller.webadmin;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.MessageContent;
import d1.project.caict.business.entity.Users;
import d1.project.caict.business.model.MessageContentFindAllVm;
import d1.project.caict.business.model.MessageContentInsertVm;
import d1.project.caict.business.model.UserFindAllByMessageContentIdVm;
import d1.project.caict.business.service.MessageService;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-14 22:04
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/messages")
public class MessageController {
    private final MessageService messageService;

    public MessageController(MessageService messageService) {
        this.messageService = messageService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<MessageContent>> findAll(@Valid MessageContentFindAllVm model) throws Exception {
        return ResultUtil.success("", messageService.findAll(model));
    }

    @GetMapping(value = "/find")
    public Result<Page<MessageContent>> findAll(@Valid @NotBlank(message = "id不能为空") String id) throws Exception {
        return ResultUtil.success("", messageService.find(id));
    }

    @GetMapping(value = "/findAllReceiver")
    public Result<Page<Users>> findAllReceiverByMessageId(@Valid UserFindAllByMessageContentIdVm model) throws Exception {
        return ResultUtil.success("", messageService.findAllReceiverByMessageId(model));
    }

    /**
     * 发送消息
     */
    @PostMapping(value = "/insert")
    public Result<String> insert(@Valid @RequestBody MessageContentInsertVm model, HttpServletRequest request) throws DoValidException {
        messageService.send(model);
        return ResultUtil.success();
    }
}
