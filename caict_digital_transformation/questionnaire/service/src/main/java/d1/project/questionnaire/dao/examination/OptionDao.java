package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:47
 */

public interface OptionDao extends JpaRepository<Option, String>, JpaSpecificationExecutor<Option> {
    /**
     * 根据试题ID查询
     * @param questionId 试题ID
     * @return 查询结果
     */
    List<Option> findAllByQuestionIdOrderByNumberAsc(String questionId);
    List<Option> findAllByQuestionIdInOrderByNumberAsc(Collection<String> questionId);

    /**
     * 根据试题ID删除选项
     * @param questionIds 试题ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByQuestionIdIn(List<String> questionIds);

    @Query(value = "select question_id,group_concat(number order by number asc) number\n" +
            "from d1_option\n" +
            "where question_id in ?1 and is_answer = '1' group by question_id", nativeQuery = true)
    Object[] findAllByQuestionAnswer(Collection<String> questionIds);
}
