package d1.project.caict.bigdata.goverance.dao.rule;

import d1.project.caict.bigdata.goverance.entity.rule.RuleLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface RuleLogDao extends JpaRepository<RuleLog, String>, JpaSpecificationExecutor<RuleLog> {

    @Query(value = "select to_char(startTime, 'YYYY-MM' ) as name, sum(checkCount) as value1,sum(errorCount) as value2 from RuleLog where startTime between ?1 and ?2 group by to_char(startTime, 'YYYY-MM')")
    List<Map<String, Long>> countGroupMonthByStartTime(Calendar createTime, Calendar createTime2);

    @Query(value = "select  coalesce(sum(checkCount),0) as checkCountSum,coalesce(sum(errorCount),0) as errorCountSum from RuleLog where startTime between ?1 and ?2")
    Map<String, Long> sumByStartTime(Calendar createTime, Calendar createTime2);

}
