package d1.project.cms.dao.repository;

import d1.project.cms.entity.repository.ResourceFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author libin
 */
public interface ResourceFileDao extends JpaRepository<ResourceFile, String>, JpaSpecificationExecutor<ResourceFile> {
    boolean existsByRepositoryId(String repositoryId);
    @Query(value = "select * from d1_resource_file where repository_id = ?1 and parent_id = ?2 order by type asc,create_time desc", nativeQuery = true)
    List<ResourceFile> findAllByRepositoryIdAndParentIdOrderByTypeAndCreateTime(String repositoryId,String parentId);
    boolean existsByParentId(String parentId);
    @Query(value = "select * from d1_resource_file where repository_id = ?1 and parent_id = ?2 and type in ?3 order by type asc,create_time desc", nativeQuery = true)
    List<ResourceFile> findAllByRepositoryIdAndParentIdAndTypeOrderByTypeAndCreateTime(String repositoryId,String parentId,List<String> type);
}
