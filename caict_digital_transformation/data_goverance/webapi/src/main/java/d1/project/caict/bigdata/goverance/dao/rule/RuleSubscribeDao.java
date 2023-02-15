package d1.project.caict.bigdata.goverance.dao.rule;

import d1.project.caict.bigdata.goverance.entity.rule.RuleSubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface RuleSubscribeDao extends JpaRepository<RuleSubscribe, String>, JpaSpecificationExecutor<RuleSubscribe> {

    Optional<RuleSubscribe> findByItcode(String itcode);

    Optional<RuleSubscribe> findByItcodeAndRuleId(String itcode, String ruleId);

    boolean existsByItcodeAndRuleId(String itcode, String ruleId);

    @Transactional
    @Modifying
    void deleteByIdAndItcode(String id, String itcode);

}
