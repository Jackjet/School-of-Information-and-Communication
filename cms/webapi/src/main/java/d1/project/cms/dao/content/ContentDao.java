package d1.project.cms.dao.content;

import d1.project.cms.entity.content.Content;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ContentDao extends JpaRepository<Content, String>, JpaSpecificationExecutor<Content> {
    List<Content> findAllByColumnIdOrderByPowerAsc(String columnId);
    boolean existsByColumnId(String columnId);
    List<Content> findAllByColumnIdAndStatusOrderByPowerAsc(String columnId,String status);

    Content findFirstByColumnId(String columnId);

    List<Content> findAllByColumnId(String columnId);

    @Query(value = "select * from d1_content where sync <> 1 or sync is null ", nativeQuery = true)
    List<Content> findBySync();

    List<Content> findAllByColumnIdInAndStatusOrderByPowerAsc(List<String> columnIds,String status);
}
