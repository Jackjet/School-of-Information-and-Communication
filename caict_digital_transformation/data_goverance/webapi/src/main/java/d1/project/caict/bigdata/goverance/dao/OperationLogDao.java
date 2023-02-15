package d1.project.caict.bigdata.goverance.dao;

import d1.project.caict.bigdata.goverance.entity.OperationLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface OperationLogDao extends JpaRepository<OperationLog, String>, JpaSpecificationExecutor<OperationLog> {

}
