package d1.project.questionnaire.view.dao;

import d1.project.questionnaire.view.entity.ExamList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface ExamListDao extends JpaRepository<ExamList, String>, JpaSpecificationExecutor<ExamList> {
}
