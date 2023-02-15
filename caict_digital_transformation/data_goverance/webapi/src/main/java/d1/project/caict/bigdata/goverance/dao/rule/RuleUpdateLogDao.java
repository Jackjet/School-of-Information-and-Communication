package d1.project.caict.bigdata.goverance.dao.rule;

import d1.project.caict.bigdata.goverance.entity.rule.RuleUpdateLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RuleUpdateLogDao extends JpaRepository<RuleUpdateLog, String>, JpaSpecificationExecutor<RuleUpdateLog> {
}
