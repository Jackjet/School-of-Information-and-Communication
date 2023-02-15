package d1.project.questionnaire.view.dao;

import d1.project.questionnaire.view.entity.QuestionBank;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author baozh
 */
public interface QuestionBankDao extends JpaRepository<QuestionBank, String>, JpaSpecificationExecutor<QuestionBank> {
}
