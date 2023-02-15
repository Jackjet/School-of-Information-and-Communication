package d1.project.caict.business.dao;

import d1.project.caict.business.entity.StatisticVisitUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;


/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface StatisticVisitUserDao extends JpaRepository<StatisticVisitUser, String>, JpaSpecificationExecutor<StatisticVisitUser> {

    /**
     * 统计系统访问人数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    @Query(value = "SELECT day, count(day) FROM (SELECT DISTINCT flag, day FROM d1_statistic_visit_user) as t1 where day BETWEEN ?1 AND ?2 GROUP BY day ORDER BY day DESC", nativeQuery = true)
    List<Map<String, String>> statisticSystemVisitUser(String startTime, String endTime);


}
