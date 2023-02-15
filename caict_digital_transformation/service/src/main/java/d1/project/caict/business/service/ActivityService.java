package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.ActivityDao;
import d1.project.caict.business.dao.MyActivityDao;
import d1.project.caict.business.dao.StatisticNewsDao;
import d1.project.caict.business.entity.Activities;
import d1.project.caict.business.entity.MyActivities;
import d1.project.caict.business.entity.StatisticNews;
import d1.project.caict.business.entity.es.News;
import d1.project.caict.business.model.ActivityFindAllVm;
import d1.project.caict.business.model.ActivityInsertVm;
import d1.project.caict.business.model.ActivityUpdateVm;
import d1.project.caict.business.model.UsersActivitiesFindAllByActivityIdVm;
import d1.project.caict.business.service.es.NewsService;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class ActivityService {

    private final ActivityDao activityDao;
    private final MyActivityDao myActivityDao;
    private final StatisticNewsDao statisticNewsDao;
    private final NewsService newsService;

    public ActivityService(ActivityDao activityDao, MyActivityDao myActivityDao, StatisticNewsDao statisticNewsDao, NewsService newsService) {
        this.activityDao = activityDao;
        this.myActivityDao = myActivityDao;
        this.statisticNewsDao = statisticNewsDao;
        this.newsService = newsService;
    }

    public Page<Activities> findAll(ActivityFindAllVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<Activities> builder = new SpecificationBuilder<>();
        return activityDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }

    public Optional<Activities> find(String id) {
        return activityDao.findById(id);
    }

    public void delete(String id) {
        this.activityDao.deleteById(id);

        //删除es里面的内容
        this.newsService.delete(id);
    }

    public void save(ActivityInsertVm model, HttpServletRequest request) throws DoValidException, ParseException {
        Activities activity = new Activities();
        BeanUtils.copyProperties(model, activity);

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(Utils.strToDate(model.getStartTime()));
        activity.setStartTime(startTime);

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(Utils.strToDate(model.getEndTime()));
        activity.setEndTime(endTime);

        activity.setId(BaseUtils.generate32Id());
        activity.setCreateTime(Calendar.getInstance());
        activity.setCreateById(Utils.getCurrentUserId(request));
        this.activityDao.save(activity);

        //插入到es数据库
        News news = new News();
        news.setId(activity.getId());
        news.setTitle(activity.getTitle());
        news.setPower(2);
        news.setType("activity");
        news.setContent(activity.getContent());
        news.setUrl(activity.getUrl());
        this.newsService.save(news);

        //插入到统计分析表
        StatisticNews statisticNews = new StatisticNews();
        statisticNews.setId(activity.getId());
        statisticNews.setCount(1);
        statisticNews.setType("最新活动");
        statisticNews.setCreateTime(activity.getCreateTime());

        Date date = activity.getCreateTime().getTime();
        statisticNews.setDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        statisticNews.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
        this.statisticNewsDao.save(statisticNews);
    }

    public void update(ActivityUpdateVm model, HttpServletRequest request) throws DoValidException, ParseException {
        Activities activity = activityDao.findById(model.getId()).orElseThrow(() -> new DoValidException("活动信息不存在"));
        BeanUtils.copyProperties(model, activity);

        Calendar startTime = Calendar.getInstance();
        startTime.setTime(Utils.strToDate(model.getStartTime()));
        activity.setStartTime(startTime);

        Calendar endTime = Calendar.getInstance();
        endTime.setTime(Utils.strToDate(model.getEndTime()));
        activity.setEndTime(endTime);

        activity.setUpdateTime(Calendar.getInstance());
        activity.setUpdateById(Utils.getCurrentUserId(request));
        this.activityDao.save(activity);

        //同步修改到es数据库
        Optional<News> newsOpt = this.newsService.find(activity.getId());
        if (!newsOpt.isPresent()) {
            return;
        }

        News news = newsOpt.get();
        news.setTitle(activity.getTitle());
        news.setContent(activity.getContent());
        news.setUrl(activity.getUrl());
        this.newsService.save(news);
    }

    /**
     * 查询用户参与的活动
     *
     * @param model model
     * @return 用户列表
     */
    public Page<MyActivities> findAllUserByActivityId(UsersActivitiesFindAllByActivityIdVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<MyActivities> builder = new SpecificationBuilder<>();
        return myActivityDao.findAll(getUsersActivitiesSpecification(paramObject, builder), builder.getPageable());
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<Activities> getSpecification(JSONObject paramObject, SpecificationBuilder<Activities> builder) throws Exception {

        SpecificationBuilder<Activities> specificationBuilder = builder.init(paramObject)
                .sContain("title", "title")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");

        String status = paramObject.getString("status");
        if (Utils.isEmpty(status)) {
            status = "-1";
        }

        SimpleDateFormat timeFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        paramObject.put("nowTime", timeFormat.format(new Date()));

        switch (status) {
            case "0":
                specificationBuilder.tThan("startTime", "nowTime", Constants.DATE_TIME_FORMAT);
                break;
            case "1":
                specificationBuilder.tLessEqual("startTime", "nowTime", Constants.DATE_TIME_FORMAT);
                specificationBuilder.tThanEqual("endTime", "nowTime", Constants.DATE_TIME_FORMAT);
                break;
            case "2":
                specificationBuilder.tLess("endTime", "nowTime", Constants.DATE_TIME_FORMAT);
                break;
            default:
                break;
        }
        return specificationBuilder.build();
    }


    /**
     * 获取查询规则
     */
    private Specification<MyActivities> getUsersActivitiesSpecification(JSONObject paramObject, SpecificationBuilder<MyActivities> builder) throws Exception {
        return builder.init(paramObject)
                .sContain("name", "name")
                .sContain("linkmanName", "linkmanName")
                .sEqual("activityId", "id")
                .dOrder("createTime")
                .build();
    }

}
