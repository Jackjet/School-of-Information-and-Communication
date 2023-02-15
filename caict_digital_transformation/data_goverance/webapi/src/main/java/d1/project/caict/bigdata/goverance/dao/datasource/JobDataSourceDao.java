package d1.project.caict.bigdata.goverance.dao.datasource;

import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * @author maoyy
 */

public interface JobDataSourceDao extends JpaRepository<JobDataSource, String>, JpaSpecificationExecutor<JobDataSource> {
    JobDataSource findFirstByNameAndCatalogAndType(String name, String catalog, String type);

    Optional<JobDataSource> findFirstByDbName(String dbName);
}
