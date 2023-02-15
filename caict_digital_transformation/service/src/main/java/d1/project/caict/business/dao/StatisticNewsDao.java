package d1.project.caict.business.dao;

import d1.project.caict.business.entity.StatisticNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface StatisticNewsDao extends JpaRepository<StatisticNews, String>, JpaSpecificationExecutor<StatisticNews> {

    /**
     * 统计新增的资讯
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
//    按类型求和
//    @Query(value = "SELECT type,sum(count) FROM (SELECT day,type,sum(count) count FROM d1_statistic_news where day BETWEEN ?1 AND ?2 GROUP BY day,type ORDER BY max(day) DESC) AS t1 GROUP BY type", nativeQuery = true)
    //按天、按类型求和
    @Query(value = "SELECT day,type,sum(count) count FROM d1_statistic_news where day BETWEEN ?1 AND ?2 GROUP BY day,type ORDER BY day DESC", nativeQuery = true)
    List<Map<String, String>> statisticNewNews(String startTime, String endTime);
}
