package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.ExaminationQuestion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:53
 */
public interface ExaminationQuestionDao extends JpaRepository<ExaminationQuestion, String>, JpaSpecificationExecutor<ExaminationQuestion> {

    /**
     * 根据考试ID查询考题
     * @param examId 考试ID
     * @return 考题信息
     */
    List<ExaminationQuestion> findAllByExaminationIdOrderByNumberAsc(String examId);

    List<ExaminationQuestion> findByExaminationIdAndQuestionIdIn(String examinationId, Collection<String> questionId);

    /**
     * 根据考试ID删除信息
     * @param examId 考试ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByExaminationId(String examId);

    boolean existsAllByQuestionIdIn(List<String> ids);
}
