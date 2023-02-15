package d1.project.caict.bigdata.goverance.dao.task;

import d1.project.caict.bigdata.goverance.entity.task.Tasks;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author  maoyy
 */
public interface TasksDao extends JpaRepository<Tasks, String>, JpaSpecificationExecutor<Tasks> {
    Tasks findByName(String name);

    List<Tasks> findByGroupTaskId(String groupTaskId);

    boolean existsByRuleId(String ruleId);
}
