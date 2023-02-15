package d1.project.cms.service.content;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.dao.content.ContentDao;
import d1.project.cms.entity.content.Content;
import d1.project.cms.enums.ContentStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author libin
 */
@Service
public class ContentService {
    private final ContentDao contentDao;

    public ContentService(ContentDao contentDao) {
        this.contentDao = contentDao;
    }

    public Page<Content> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Content> builder = new SpecificationBuilder<>();
        Specification<Content> specification = builder.init(params)
                .sEqual("status", "status")
                .sContain("title", "title")
                .sEqual("columnId", "columnId")
                .aOrder("power").build();

        return this.contentDao.findAll(specification, builder.getPageable());
    }

    public Content findById(String id) throws DoValidException {
        return this.contentDao.findById(id).orElseThrow(() -> new DoValidException("内容不存在"));
    }

    public void save(Content content) {
        this.contentDao.save(content);
    }

    public void delete(Content content) {
        this.contentDao.delete(content);
    }

    public List<Content> findAllByColumnIdAndStatusOrderByPowerAsc(String columnId, String status) {
        return this.contentDao.findAllByColumnIdAndStatusOrderByPowerAsc(columnId, status);
    }

    public Content findFirstByColumnId(String columnId) {
        return this.contentDao.findFirstByColumnId(columnId);
    }

    public boolean existsByColumnId(String columnId) {
        return this.contentDao.existsByColumnId(columnId);
    }

    public List<Content> findAllByColumnId(String columnId) {
        return this.contentDao.findAllByColumnId(columnId);
    }

    public List<Content> findBySync() {
        return this.contentDao.findBySync();
    }

    public List<Content> findAllByColumnIdInAndStatusOrderByPowerAsc(List<String> columnIds,String status) throws Exception {

        return this.contentDao.findAllByColumnIdInAndStatusOrderByPowerAsc(columnIds, status);
    }

    public void batchDelete(List<Content> contents) {
        this.contentDao.deleteAll(contents);
    }
}
