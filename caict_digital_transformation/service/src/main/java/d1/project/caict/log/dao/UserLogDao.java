package d1.project.caict.log.dao;

import d1.project.caict.log.entity.UserLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 16:38
 */
public interface UserLogDao extends JpaRepository<UserLog, String>, JpaSpecificationExecutor<UserLog> {
}
