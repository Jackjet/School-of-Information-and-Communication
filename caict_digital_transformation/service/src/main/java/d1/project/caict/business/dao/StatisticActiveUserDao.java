package d1.project.caict.business.dao;

import d1.project.caict.business.entity.StatisticActiveUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface StatisticActiveUserDao extends JpaRepository<StatisticActiveUser, String>, JpaSpecificationExecutor<StatisticActiveUser> {


    /**
     * 查询活跃用户数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    @Query(value = "SELECT visit_date, count(visit_date) FROM (SELECT DISTINCT user_id, visit_date FROM d1_statistic_active_user) as t1 where visit_date BETWEEN ?1 AND ?2 GROUP BY visit_date ORDER BY visit_date DESC", nativeQuery = true)
    List<Map<String, String>> statisticActiveUser(String startTime, String endTime);
}
