package d1.project.caict.bigdata.goverance.dao.task;

import d1.project.caict.bigdata.goverance.entity.task.GroupTaskLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author maoyy
 */
public interface GroupTaskLogDao extends JpaRepository<GroupTaskLog, String>, JpaSpecificationExecutor<GroupTaskLog> {

}
