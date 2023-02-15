package d1.project.caict.business.service.es;

import com.alibaba.fastjson.JSON;
import d1.project.caict.business.dao.es.NewsDao;
import d1.project.caict.business.entity.es.News;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.index.query.MatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.SearchHits;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightBuilder;
import org.elasticsearch.search.fetch.subphase.highlight.HighlightField;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.SearchResultMapper;
import org.springframework.data.elasticsearch.core.aggregation.AggregatedPage;
import org.springframework.data.elasticsearch.core.aggregation.impl.AggregatedPageImpl;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/10/9
 */
@Service
public class NewsService {

    private final NewsDao newsDao;
    private final ElasticsearchTemplate elasticsearchTemplate;

    public NewsService(NewsDao newsDao, ElasticsearchTemplate elasticsearchTemplate) {
        this.newsDao = newsDao;
        this.elasticsearchTemplate = elasticsearchTemplate;
    }

    public long count() {
        return newsDao.count();
    }

    public News save(News news) {
        return newsDao.save(news);
    }

    public Iterable<News> batchSave(Iterable<News> news) {
        return newsDao.saveAll(news);
    }

    public void delete(String id) {
        newsDao.deleteById(id);
    }

    public Optional<News> find(String id) {
        return newsDao.findById(id);
    }

    public Iterable<News> getAll() {
        return newsDao.findAll();
    }

    public List<News> getByName(String name) {
        List<News> list = new ArrayList<>();
        MatchQueryBuilder matchQueryBuilder = new MatchQueryBuilder("content", name);
        Iterable<News> iterable = newsDao.search(matchQueryBuilder);
        iterable.forEach(e -> list.add(e));
        return list;
    }

    public Page<News> pageQuery(String kw, Pageable pageable) {
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
//                .withQuery(QueryBuilders.matchPhraseQuery("title", kw))
                .withQuery(QueryBuilders.multiMatchQuery(kw, "title", "content"))
//                .withQuery(QueryBuilders.queryStringQuery(kw))
                .withPageable(pageable)
                .build();
        return newsDao.search(searchQuery);
    }

    public Page<News> pageQueryWithHighlight(String keyword, Pageable pageable) {
        String preTag = "<font color='#dd4b39'>";//google的色值
        String postTag = "</font>";

        Integer page = pageable.getPageNumber();
        Integer size = pageable.getPageSize();
        page = (page == null || page < 1) ? 0 : page - 1;
        if (size == null || size < 1) {
            size = 10;
        }

        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(keyword, "title", "content"))
                .withHighlightFields(new HighlightBuilder.Field("title").preTags(preTag).postTags(postTag), new HighlightBuilder.Field("content").preTags(preTag).postTags(postTag))
                .withPageable(PageRequest.of(page, size, pageable.getSort()))
                .build();

        // 高亮字段
        return elasticsearchTemplate.queryForPage(searchQuery, News.class, new SearchResultMapper() {
            @Override
            public <T> AggregatedPage<T> mapResults(SearchResponse response, Class<T> clazz, Pageable pageable) {
                List<News> chunk = new ArrayList<>();
                SearchHits searchHits = response.getHits();
                if (searchHits == null || searchHits.totalHits == 0) {
                    return new AggregatedPageImpl<>((List<T>) chunk, pageable, searchHits.totalHits);
                }

                for (SearchHit searchHit : searchHits.getHits()) {
                    News news = JSON.parseObject(searchHit.getSourceAsString(), News.class);

                    HighlightField titleField = searchHit.getHighlightFields().get("title");
                    if (titleField != null) {
                        news.setTitle(titleField.fragments()[0].toString());
                    }
                    HighlightField contentField = searchHit.getHighlightFields().get("content");
                    if (contentField != null) {
                        news.setContent(contentField.fragments()[0].toString());
                    }
                    chunk.add(news);
                }
                return new AggregatedPageImpl<>((List<T>) chunk, pageable, searchHits.totalHits);
            }
        });
    }
}