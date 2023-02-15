package d1.project.caict.bigdata.goverance.dao.rule;

import d1.project.caict.bigdata.goverance.entity.rule.Rule;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Calendar;

public interface RuleDao extends JpaRepository<Rule, String>, JpaSpecificationExecutor<Rule> {
    Long countByCreateTimeGreaterThanEqualAndCreateTimeLessThanEqual(Calendar createTime, Calendar createTime2);

    boolean existsByTemplateId(String templateId);
    boolean existsByName(String name);
}
