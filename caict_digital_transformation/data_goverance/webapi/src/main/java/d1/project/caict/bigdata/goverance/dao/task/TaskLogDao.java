package d1.project.caict.bigdata.goverance.dao.task;

import d1.project.caict.bigdata.goverance.entity.task.TaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.Calendar;
import java.util.Collection;
import java.util.List;
import java.util.Map;

/**
 * @author maoyy
 */

public interface TaskLogDao extends JpaRepository<TaskLog, String>, JpaSpecificationExecutor<TaskLog> {
    Long countByStartTimeGreaterThanEqualAndStartTimeLessThanEqual(Calendar startTime, Calendar startTime2);


    @Query(value = "select to_char(startTime, 'YYYY-MM' ) as name, count(id) as value from TaskLog where startTime between ?1 and ?2 group by to_char(startTime, 'YYYY-MM')")
    List<Map<String,Long>> countGroupMonthByStartTime(Calendar createTime, Calendar createTime2);

    long countByGroupTaskLogId(String groupTaskLogId);
    long countByGroupTaskLogIdAndStatusIn(String groupTaskLogId, Collection<String> status);
}