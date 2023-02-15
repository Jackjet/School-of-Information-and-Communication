package d1.project.caict.business.dao;

import d1.project.caict.business.entity.Messages;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface MessageDao extends JpaRepository<Messages, String>, JpaSpecificationExecutor<Messages> {

    /**
     * 查看此条消息的接收者
     *
     * @param messageContentId 消息内容id
     * @return 消息发送的列表
     */
    @Query(value = "select receiver_id from d1_messages where message_content_id =(?1)", nativeQuery = true)
    List<String> findAllByMessageContentId(String messageContentId);

    /**
     * 查询我的消息列表
     */
    <T> Page<T> execSqlWithPage(@NotNull String sql, @NotNull Pageable pageable, @NotNull Class<T> clazz);

    /**
     * 标记消息已读
     */
    @Modifying
    @Query(value = "update d1_messages set status =?1 where id =?2 and receiver_id =?3", nativeQuery = true)
    void updateStatus(String status, String messageContentId, String userId);
}