package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.MyActivityDao;
import d1.project.caict.business.entity.MyActivities;
import d1.project.caict.business.entity.Users;
import d1.project.caict.business.model.MyActivityInsertVm;
import d1.project.caict.business.model.MyActivityResultVm;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class MyActivityService {
    private final MyActivityDao myActivityDao;
    private final UserService userService;

    public MyActivityService(MyActivityDao myActivityDao, UserService userService) {
        this.myActivityDao = myActivityDao;
        this.userService = userService;
    }

    /**
     * 我的活动列表
     */
    public Page<MyActivityResultVm> findAll(PageableVm model, HttpServletRequest request) throws Exception {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        String sql = "SELECT  " +
                "my_activities.id as id, " +
                "my_activities.activity_id as activityId, " +
                "my_activities.industries as industries, " +
                "my_activities.name as name, " +
                "my_activities.user_id as userId, " +
                "my_activities.linkman_name as linkmanName, " +
                "my_activities.linkman_phone as linkmanPhone, " +
                "activities.start_time as activityStartTime, " +
                "activities.end_time as activityEndTime, " +
                "activities.title as activityTitle, " +
                "activities.content as activityContent, " +
                "activities.pic as activityPic, " +
                "activities.url as activityUrl " +
                "FROM  " +
                "d1_my_activities as my_activities " +
                "LEFT JOIN " +
                "d1_activities as activities  " +
                "ON  " +
                "my_activities.activity_id = activities.id  " +
                "WHERE " +
                "my_activities.user_id = '" + userId + "'  " +
                "ORDER BY my_activities.create_time DESC";

        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<MyActivityResultVm> builder = new SpecificationBuilder<>();
        builder.init(paramObject);
        return myActivityDao.execSqlWithPage(sql, builder.getPageable(), MyActivityResultVm.class);
    }

    public Optional<MyActivities> find(String activityId, HttpServletRequest request) throws DoValidException {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        return myActivityDao.findFirstByActivityIdAndUserId(activityId, userId);
    }

    public void delete(String id) {
        this.myActivityDao.deleteById(id);
    }

    public void save(MyActivityInsertVm model, HttpServletRequest request) throws DoValidException {
        //更加用户Id 查找用户信息
        Users user = userService.find(model.getUserId()).orElseThrow(() -> new DoValidException("加入失败，用户信息不存在!"));
        //判断此用户是否已经参加
        Optional<MyActivities> myActivity = myActivityDao.findFirstByActivityIdAndUserId(model.getActivityId(), model.getUserId());
        if (myActivity.isPresent()) {
            throw new DoValidException("加入失败，用户已经参加!");
        }

        MyActivities entity = new MyActivities();
        entity.setUserId(user.getId());
        entity.setName(user.getName());
        entity.setLinkmanName(user.getLinkmanName());
        entity.setLinkmanPhone(user.getLinkmanPhone());
        entity.setIndustries(user.getIndustries());
        entity.setActivityId(model.getActivityId());
        entity.setUrl(model.getUrl());

        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        this.myActivityDao.save(entity);
    }

    ////////////////////////////////////////////////////////
}
