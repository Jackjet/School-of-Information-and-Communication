package d1.project.caict.bigdata.goverance.dao;

import d1.project.caict.bigdata.goverance.entity.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

public interface TemplateDao extends JpaRepository<Template, String>, JpaSpecificationExecutor<Template> {

    @Query(value = "select constraintType as name,count(constraintType) as value\n" +
            "from Template \n" +
            "where  ?1 <= createTime and ?2  >= createTime\n  "+
            "group by constraintType")
    List<Map<String, Object>> countGroupConstraintTypeByCreateTime(Calendar createTime, Calendar createTime2);

    @Query(value = "select tle.constraintType              as name,\n" +
            "       sum(rl.checkCount) as value1,\n" +
            "       sum(rl.errorCount) as value2 \n" +
            "from Template as tle left join RuleLog as rl on tle.id = rl.templateId\n" +
            "             where  ?1 <= rl.startTime and ?2  >= rl.startTime\n" +
            "group by tle.constraintType")
    List<Map<String, Object>> countGroupConstraintTypeByRuleLogCreateTime(Calendar createTime, Calendar createTime2);

    @Query(value = "select tle.constraintType              as name,\n" +
            "       sum(rl.checkCount) as value1,\n" +
            "       sum(rl.errorCount) as value2 \n" +
            "from  RuleLog as rl left join Template as tle  on tle.id = rl.templateId " +
            "group by tle.constraintType")
    List<Map<String, Object>> countGroupConstraintTypeByRuleLogCreateTime();

    boolean existsByName(String name);

    boolean existsByDataQualityType(String dataQualityType);
}
