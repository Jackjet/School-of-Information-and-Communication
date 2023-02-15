package d1.project.caict.business.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.caict.business.dao.*;
import d1.project.caict.business.entity.StatisticServiceVisitCount;
import d1.project.caict.business.entity.StatisticVisitUser;
import d1.project.caict.business.model.StatisticServiceVisitCountInsertVm;
import d1.project.caict.business.model.StatisticVisitUserInsertVm;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class StatisticService {

    private final UserDao userDao;
    private final StatisticActiveUserDao statisticActiveUserDao;
    private final StatisticNewsDao statisticNewsDao;
    private final StatisticVisitUserDao statisticVisitUserDao;
    private final StatisticServiceVisitCountDao statisticServiceVisitCountDao;

    public StatisticService(UserDao userDao, StatisticActiveUserDao statisticActiveUserDao, StatisticNewsDao statisticNewsDao, StatisticVisitUserDao statisticVisitUserDao, StatisticServiceVisitCountDao statisticServiceVisitCountDao) {
        this.userDao = userDao;
        this.statisticActiveUserDao = statisticActiveUserDao;
        this.statisticNewsDao = statisticNewsDao;
        this.statisticVisitUserDao = statisticVisitUserDao;
        this.statisticServiceVisitCountDao = statisticServiceVisitCountDao;
    }

    /**
     * 统计活跃用户数，今天登录就算活跃
     */
    public List<Map<String, String>> statisticActiveUser(String startTime, String endTime) {
        return this.statisticActiveUserDao.statisticActiveUser(startTime, endTime);
    }

    /**
     * 统计系统访问人数
     */
    public List<Map<String, String>> statisticSystemVisitUser(String startTime, String endTime) {
        return this.statisticVisitUserDao.statisticSystemVisitUser(startTime, endTime);
    }

    /**
     * 统计服务访问量
     */
    public List<Map<String, String>> statisticServiceVisitCount(String startTime, String endTime) {
        return this.statisticServiceVisitCountDao.statisticServiceVisitCount(startTime, endTime);
    }

    /**
     * 统计新增用户数
     */
    public List<Map<String, String>> statisticNewUser(String startTime, String endTime) {
        return this.userDao.statisticNewUser(startTime, endTime);
    }

    /**
     * 统计新增新闻数
     */
    public Map<String, JSONObject> statisticNewNews(String startTime, String endTime) {
        List<Map<String, String>> list = this.statisticNewsDao.statisticNewNews(startTime, endTime);
        Map<String, JSONObject> result = new HashMap<>();
        if (list == null) {
            return result;
        }
        for (int i = 0; i < list.size(); i++) {
            Map<String, String> row = list.get(i);
            String type = null;
            if (row.containsKey("type")) {
                type = row.get("type");
            }

            JSONObject jsonObject = result.get(type);
            if (jsonObject == null) {
                jsonObject = new JSONObject();
                result.put(type, jsonObject);
            }
            jsonObject.put(row.get("day"), row.get("count"));
        }
        return result;
    }


    /**
     * 统计用户来源
     */
    public Map<String, Object> statisticUserSource() {
        List<Map<String, String>> list = this.userDao.statisticUserSource();
        long totalCount = this.userDao.count();

        Map<String, Object> content = new HashMap<>();
        for (Map<String, String> map : list) {
            long count = Long.parseLong(String.valueOf(map.get("count")));
            double percent = new BigDecimal((double) count / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

            JSONObject data = new JSONObject();
            data.put("count", count);
            data.put("percent", percent);
            content.put(map.get("source"), data);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", totalCount);
        result.put("content", content);
        return result;
    }

    /**
     * 统计用户所在地
     */
    public Map<String, Object> statisticUserAddress() {
        List<Map<String, String>> list = this.userDao.statisticUserAddress();
        long totalCount = this.userDao.count();

        Map<String, Object> content = new HashMap<>();
        for (Map<String, String> map : list) {
            long count = Long.parseLong(String.valueOf(map.get("count")));
            double percent = new BigDecimal((double) count / totalCount).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();

            JSONObject data = new JSONObject();
            data.put("count", count);
            data.put("percent", percent);
            content.put(map.get("province"), data);
        }

        Map<String, Object> result = new HashMap<>();
        result.put("total", totalCount);
        result.put("content", content);
        return result;
    }

    //系统访问人数
    public void saveSystemVisitUser(StatisticVisitUserInsertVm model) {
        StatisticVisitUser entity = new StatisticVisitUser();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setVisitDate(Calendar.getInstance());

        Date date = entity.getVisitDate().getTime();
        entity.setDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        entity.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
        this.statisticVisitUserDao.save(entity);
    }

    //服务访问量
    public void saveStatisticServiceVisitCount(StatisticServiceVisitCountInsertVm model) {
        StatisticServiceVisitCount entity = new StatisticServiceVisitCount();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setVisitDate(Calendar.getInstance());

        Date date = entity.getVisitDate().getTime();
        entity.setDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        entity.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
        this.statisticServiceVisitCountDao.save(entity);
    }
}
