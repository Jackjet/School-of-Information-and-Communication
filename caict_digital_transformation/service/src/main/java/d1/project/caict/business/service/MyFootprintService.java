package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.model.PageableVm;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.MyFootprintDao;
import d1.project.caict.business.entity.MyFootprint;
import d1.project.caict.business.model.MyFootprintFindAllVm;
import d1.project.caict.business.model.MyFootprintInsertVm;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class MyFootprintService {
    private final MyFootprintDao myFootprintDao;

    public MyFootprintService(MyFootprintDao myFootprintDao) {
        this.myFootprintDao = myFootprintDao;
    }

    public Page<MyFootprintFindAllVm> findAll(PageableVm model, HttpServletRequest request) throws Exception {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }

        String sql = "SELECT " +
                "day " +
                "FROM " +
                "d1_my_footprint " +
                "WHERE " +
                "user_id = '" + userId + "' " +
                "GROUP BY day " +
                "ORDER BY max(create_time) DESC";

        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<MyFootprintFindAllVm> builder = new SpecificationBuilder<>();
        builder.init(paramObject);
        Page<MyFootprintFindAllVm> days = myFootprintDao.execSqlWithPage(sql, builder.getPageable(), MyFootprintFindAllVm.class);

        List<MyFootprintFindAllVm> content = days.getContent();
        for (MyFootprintFindAllVm myFootprintFindAllVm : content) {
            List<MyFootprint> myFootprintList = myFootprintDao.findAllByUserIdAndDayOrderByCreateTimeDesc(userId, myFootprintFindAllVm.getDay());
            System.out.println(myFootprintFindAllVm.getDay() + ": " + myFootprintList.size());
            myFootprintFindAllVm.setChild(myFootprintList);
        }
        return days;
    }

    public Optional<MyFootprint> find(String id) {
        return myFootprintDao.findById(id);
    }

    public void delete(String id) {
        this.myFootprintDao.deleteById(id);
    }

    public void save(MyFootprintInsertVm model, HttpServletRequest request) throws DoValidException {
        MyFootprint entity = new MyFootprint();
        BeanUtils.copyProperties(model, entity);

        Calendar calendar = Calendar.getInstance();
        Date date = calendar.getTime();

        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(calendar);
        entity.setCreateById(Utils.getCurrentUserId(request));
        entity.setDay(new SimpleDateFormat("yyyy-MM-dd").format(date));
        entity.setTime(new SimpleDateFormat("HH:mm:ss").format(date));
        this.myFootprintDao.save(entity);
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<MyFootprint> getSpecification(JSONObject paramObject, SpecificationBuilder<MyFootprint> builder) throws Exception {
        SpecificationBuilder<MyFootprint> specificationBuilder = builder.init(paramObject)
                .sEqual("userId", "userId")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");
        return specificationBuilder.build();
    }
}
