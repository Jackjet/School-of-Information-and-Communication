package d1.project.caict.business.dao.es;

import d1.project.caict.business.entity.es.News;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

/**
 * @author: maorui
 * @date: 2020/10/9
 */
@Repository
public interface NewsDao extends ElasticsearchRepository<News, String> {



}
