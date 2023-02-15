package d1.project.caict.business.dao;

import d1.project.caict.business.entity.StatisticServiceVisitCount;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface StatisticServiceVisitCountDao extends JpaRepository<StatisticServiceVisitCount, String>, JpaSpecificationExecutor<StatisticServiceVisitCount> {

    /**
     * 统计系统访问人数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    @Query(value = "SELECT type, sum(count) from (SELECT day,type, count(day) FROM d1_statistic_service_visit_count where day BETWEEN ?1 AND ?2 GROUP BY day , type ORDER BY day DESC) AS t1 GROUP BY type", nativeQuery = true)
    List<Map<String, String>> statisticServiceVisitCount(String startTime, String endTime);

}
