package d1.project.cms.dao.column;

import d1.project.cms.entity.column.Column;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author libin
 */
public interface ColumnDao extends JpaRepository<Column, String>, JpaSpecificationExecutor<Column> {
    List<Column> findAllByParentIdOrderByPower(String parentId);

    /**
     * 根据模板ID查询信息
     * @param templateId 模板ID
     * @return 查询结果
     */
    boolean existsByTemplateId(String templateId);

    boolean existsByParentId(String parentId);
    boolean existsByParentIdAndColumnName(String parentId,String columnName);

    boolean existsByParentIdAndColumnNameAndIdNot(String parentId,String columnName,String id);

    List<Column> findAllByTemplateIdIn(List<String> templateIds);
    Column findFirstByColumnId(String columnId);

    @Query(value = "select id from d1_column where parent_id = ?1", nativeQuery = true)
    List<String> findAllColumnIdsByParentId(String parentId);
}
