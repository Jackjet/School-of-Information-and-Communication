package d1.project.cms.dao;

import d1.project.cms.entity.SampleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Buter
 */
public interface SampleDao extends JpaRepository<SampleEntity, String>, JpaSpecificationExecutor<SampleEntity> {
}