package d1.project.questionnaire.view.dao;

import d1.project.questionnaire.view.entity.ExamGroupNum;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;


/**
 * @author baozh
 */
public interface ExamGroupNumDao extends JpaRepository<ExamGroupNum, String>, JpaSpecificationExecutor<ExamGroupNum> {
}
