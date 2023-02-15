package d1.project.cms.dao.column;

import d1.project.cms.entity.column.ColumnAttachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;

/**
 * @author libin
 */
public interface ColumnAttachmentDao extends JpaRepository<ColumnAttachment, String>, JpaSpecificationExecutor<ColumnAttachment> {
    List<ColumnAttachment> findAllByRelatedIdOrderByCreateTime(String relatedId);
    ColumnAttachment findFirstByRelatedId(String relatedId);
}
