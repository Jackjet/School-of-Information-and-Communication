package d1.project.cms.dao.repository;

import d1.project.cms.entity.repository.Repository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author libin
 */
public interface RepositoryDao extends JpaRepository<Repository, String>, JpaSpecificationExecutor<Repository> {
    boolean existsByName(String name);
    boolean existsByNameAndIdNot(String name,String id);
}
