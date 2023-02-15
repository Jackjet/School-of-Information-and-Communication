package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.MessageContentDao;
import d1.project.caict.business.dao.MessageDao;
import d1.project.caict.business.dao.UserDao;
import d1.project.caict.business.entity.MessageContent;
import d1.project.caict.business.entity.Messages;
import d1.project.caict.business.entity.Users;
import d1.project.caict.business.model.*;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.service.IOperationLogService;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class MessageService {

    private final MessageContentDao messageContentDao;
    private final MessageDao messageDao;
    private final UserDao userDao;
    private final IOperationLogService iOperationLogService;

    public MessageService(MessageContentDao messageContentDao, MessageDao messageDao, UserDao userDao, IOperationLogService operationLogService) {
        this.messageContentDao = messageContentDao;
        this.messageDao = messageDao;
        this.userDao = userDao;
        this.iOperationLogService = operationLogService;
    }

    public Page<MessageContent> findAll(MessageContentFindAllVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<MessageContent> builder = new SpecificationBuilder<>();
        return messageContentDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }

    /**
     * 根据 messageContentId 查找对应的接收方
     */
    public Page<Users> findAllReceiverByMessageId(UserFindAllByMessageContentIdVm model) throws Exception {
        String messageContentId = model.getId();
        List<String> receiverIds = this.messageDao.findAllByMessageContentId(messageContentId);

        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        paramObject.put("receiverIds", receiverIds);

        SpecificationBuilder<Users> builder = new SpecificationBuilder<>();
        Specification<Users> specification = builder.init(paramObject)
                .sIn("id", "receiverIds")
                .sContain("name", "name")
                .sContain("linkmanName", "linkmanName")
                .sContain("source", "source")
                .dOrder("createTime")
                .build();

        return userDao.findAll(specification, builder.getPageable());
    }

    /**
     * 我的消息列表
     */
    public Page<MyMessageContentVm> findAllByUser(MessageContentFindAllByUserIdVm model, HttpServletRequest request) throws Exception {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }

        String sql = "SELECT " +
                "msg.id as id, " +
                "msg.receiver_id as receiverId, " +
                "msg.status as status, " +
                "msg_content.id as messageId, " +
                "msg_content.create_time as time, " +
                "msg_content.title as title, " +
                "msg_content.text as text, " +
                "msg_content.type as type, " +
                "msg_content.sender_id as senderId " +
                "FROM " +
                "d1_messages as msg " +
                "LEFT JOIN " +
                "d1_message_content as msg_content " +
                "ON " +
                "msg_content.id = msg.message_content_id " +
                "WHERE " +
                "msg.receiver_id = '" + userId + "' " +
                "AND msg.status = '" + model.getStatus() + "' " +
                "ORDER BY time DESC";

        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<MyMessageContentVm> builder = new SpecificationBuilder<>();
        builder.init(paramObject);
        return this.messageDao.execSqlWithPage(sql, builder.getPageable(), MyMessageContentVm.class);
    }

    public Optional<MessageContent> find(String id) {
        return messageContentDao.findById(id);
    }

    public void delete(String id) {
        this.messageContentDao.deleteById(id);
    }

    public void send(MessageContentInsertVm model) {
        String senderId = model.getSenderId();

        //先插入一条数据到MessageContent表中
        MessageContent messageContent = new MessageContent();
        messageContent.setId(BaseUtils.generate32Id());
        messageContent.setSenderId(senderId);
        messageContent.setTitle(model.getTitle());
        messageContent.setText(model.getText());
        messageContent.setType("0");
        messageContent.setCreateById(senderId);
        messageContent.setCreateTime(Calendar.getInstance());
        messageContentDao.save(messageContent);

        Collection<String> userIds = model.getUserIds();
        List<Messages> messages = new ArrayList<>();
        for (String userId : userIds) {
            Messages message = new Messages();
            message.setId(BaseUtils.generate32Id());
            message.setMessageContentId(messageContent.getId());
            message.setStatus("0");
            message.setReceiverId(userId);
            messages.add(message);
        }
        messageDao.saveAll(messages);
    }

    /**
     * 标记消息已读
     */
    @Transactional(rollbackFor = Exception.class)
    public void read(String messageContentId, HttpServletRequest request) throws DoValidException {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        messageDao.updateStatus("1", messageContentId, userId);
    }

    ///////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<MessageContent> getSpecification(JSONObject paramObject, SpecificationBuilder<MessageContent> builder) throws Exception {
        SpecificationBuilder<MessageContent> specificationBuilder = builder.init(paramObject)
                .sContain("title", "title")
                .sContain("text", "text")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");
        return specificationBuilder.build();
    }
}
