package d1.project.caict.business.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.caict.business.dao.StatisticNewsDao;
import d1.project.caict.business.entity.StatisticNews;
import d1.project.caict.business.entity.es.News;
import d1.project.caict.business.service.es.NewsService;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.utils.BaseUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @author: maorui
 * @date: 2020/10/12
 */
@Service
public class CallbackService {
    private static final Logger logger = LoggerFactory.getLogger(CallbackService.class);

    private final NewsService newsService;
    private final StatisticNewsDao statisticNewsDao;

    @Value("${caict.news.column-id.cydt:3cc6bde6eedc491ba681b697bc1e2664}")
    private String cydtColumnId;

    @Value("${caict.news.column-id.hyzx:ac4e23a4105a4bf9bbf6dd076bc616cf}")
    private String hyzxColumnId;

    @Value("${caict.news.column-id.zcjd:261fdb14bd3f4b748b6340ab9d87fc98}")
    private String zcjdColumnId;

    public CallbackService(NewsService newsService, StatisticNewsDao statisticNewsDao) {
        this.newsService = newsService;
        this.statisticNewsDao = statisticNewsDao;
    }

    public void cmsCallback(List<News> news, HttpServletRequest request) throws Exception {
        //验证appid，appkey
        String auth = request.getHeader("authorization");
        if (Utils.isEmpty(auth)) {
            throw new DoValidException("header里面需要包含authorization参数");
        }

        if (!auth.contains("sign ") || auth.split(" ").length < 2) {
            throw new DoValidException("authorization格式应该是sign xxxx");
        }

        String appId = request.getHeader("appId");
        if (Utils.isEmpty(appId)) {
            throw new DoValidException("header里面需要包含appId参数");
        }

        String timestamp = request.getHeader("timestamp");
        if (Utils.isEmpty(timestamp)) {
            throw new DoValidException("header里面需要包含timestamp参数");
        }

        String appKey = Utils.getAppKey(appId);
        if (StringUtils.isEmpty(appKey)) {
            throw new DoValidException("找不到对应的appId = " + appId);
        }

        if (!Utils.verfiyHmacSign(auth, appId, timestamp, appKey)) {
            throw new DoValidException("签名验证失败");
        }

        if (news == null || news.size() == 0) {
            return;
        }
        logger.info("appId= " + appId + ", insert es " + news.size());
        newsService.batchSave(news);

        //插入到统计分析表
        StatisticNews statisticNews = new StatisticNews();
        statisticNews.setId(BaseUtils.generate32Id());
        statisticNews.setCount(news.size());
        statisticNews.setType(getType(news.get(0)));
        statisticNews.setCreateTime(Calendar.getInstance());

        Date date = statisticNews.getCreateTime().getTime();
        statisticNews.setDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        statisticNews.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
        this.statisticNewsDao.save(statisticNews);
    }

    private String getType(News news) {
        if (news == null) {
            return "未知";
        }
//        产业动态    3cc6bde6eedc491ba681b697bc1e2664
//        行业资讯    ac4e23a4105a4bf9bbf6dd076bc616cf
//        政策解读    261fdb14bd3f4b748b6340ab9d87fc98
        String columnId = news.getColumnId();
        if (cydtColumnId.equals(columnId)) {
            return "行业资讯";
        } else if (hyzxColumnId.equals(columnId)) {
            return "政策解读";
        } else if (zcjdColumnId.equals(columnId)) {
            return "产业动态";
        } else {
            return "未知";
        }
    }
}
