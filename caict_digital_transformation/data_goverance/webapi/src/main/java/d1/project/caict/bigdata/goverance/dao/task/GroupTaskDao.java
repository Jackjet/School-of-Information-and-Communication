package d1.project.caict.bigdata.goverance.dao.task;

import d1.project.caict.bigdata.goverance.entity.task.GroupTask;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author maoyy
 */
public interface GroupTaskDao extends JpaRepository<GroupTask, String>, JpaSpecificationExecutor<GroupTask> {

     GroupTask findByName(String name);

     boolean existsByDataSourceSourceIdOrDataSourceTargetId(String dataSourceSourceId, String dataSourceTargetId);

}
