package d1.project.caict.log.dao;

import d1.project.caict.log.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 09:48
 */
public interface OperationLogDao extends JpaRepository<OperationLog, String>, JpaSpecificationExecutor<OperationLog> {
}
