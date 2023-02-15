package d1.project.cms.service.column;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.cms.dao.column.ColumnAttachmentDao;
import d1.project.cms.entity.column.ColumnAttachment;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 */
@Service
public class ColumnAttachmentService {
    private final ColumnAttachmentDao columnAttachmentDao;

    public ColumnAttachmentService(ColumnAttachmentDao columnAttachmentDao) {
        this.columnAttachmentDao = columnAttachmentDao;
    }

    public void save(ColumnAttachment columnAttachment){
        this.columnAttachmentDao.save(columnAttachment);
    }

    public List<ColumnAttachment> findAllByRelatedId(String relatedId){
        return this.columnAttachmentDao.findAllByRelatedIdOrderByCreateTime(relatedId);
    }

    public void batchDelete(List<ColumnAttachment> columnAttachments){
        this.columnAttachmentDao.deleteInBatch(columnAttachments);
    }

    public ColumnAttachment findById(String id) throws DoValidException {
        return this.columnAttachmentDao.findById(id).orElseThrow(() -> new DoValidException("附件记录不存在"));
    }

    public ColumnAttachment findFirstByRelatedId(String relatedId){
        return this.columnAttachmentDao.findFirstByRelatedId(relatedId);
    }

    public void bacthSave(List<ColumnAttachment> columnAttachments){
        this.columnAttachmentDao.saveAll(columnAttachments);
    }
}
