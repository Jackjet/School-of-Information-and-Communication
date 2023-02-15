package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:45
 */

public interface QuestionDao extends JpaRepository<Question, String>, JpaSpecificationExecutor<Question> {

    /**
     * 根据考试分组ID查询是否存在
     * @param id id
     * @return 查询结果
     */
    boolean existsAllByExaminationGroupId(String id);

    /**
     * 根据ID集合批量删除
     * @param ids ID集合
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByIdIn(List<String> ids);

    /**
     * 根据数据ID批量修改分组ID
     * @param groupId 分组ID
     * @param ids 数据ID
     */
    @Query(value = "UPDATE d1_question SET examination_group_id = ?1 WHERE id IN ?2",nativeQuery = true)
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void updateGroupIdByIds(String groupId,List<String> ids);

    /**
     * 根据ID集合查询试题
     * @param ids ID集合
     * @return 查询结果
     */
    List<Question> findAllByIdInOrderByCreateTimeDesc(List<String> ids);
}
