package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.MyCollectDao;
import d1.project.caict.business.entity.MyCollect;
import d1.project.caict.business.model.MyCollectFindAllVm;
import d1.project.caict.business.model.MyCollectInsertVm;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class MyCollectService {

    private final MyCollectDao myCollectDao;

    public MyCollectService(MyCollectDao myCollectDao) {
        this.myCollectDao = myCollectDao;
    }

    public Page<MyCollect> findAll(MyCollectFindAllVm model, HttpServletRequest request) throws Exception {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        paramObject.put("userId", userId);
        SpecificationBuilder<MyCollect> builder = new SpecificationBuilder<>();
        return myCollectDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }

    public Optional<MyCollect> find(String cmsContentId, HttpServletRequest request) throws DoValidException {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        return myCollectDao.findFirstByCmsContentIdAndUserId(cmsContentId, userId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void cancel(String cmsContentId, HttpServletRequest request) throws DoValidException {
        String userId = Utils.getCurrentUserId(request);
        if (Utils.isEmpty(userId)) {
            throw new DoValidException("用户未登录");
        }
        this.myCollectDao.deleteByCmsContentIdAndUserId(cmsContentId, userId);
    }

    public void delete(String id) {
        this.myCollectDao.deleteById(id);
    }

    public void save(MyCollectInsertVm model, HttpServletRequest request) throws DoValidException {
        //判断此用户是否已经收藏
        Optional<MyCollect> myActivity = myCollectDao.findFirstByCmsContentIdAndUserId(model.getCmsContentId(), model.getUserId());
        if (myActivity.isPresent()) {
            throw new DoValidException("收藏失败，用户已经收藏!");
        }
        MyCollect entity = new MyCollect();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setCmsContentId(model.getCmsContentId());
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        this.myCollectDao.save(entity);
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<MyCollect> getSpecification(JSONObject paramObject, SpecificationBuilder<MyCollect> builder) throws Exception {
        SpecificationBuilder<MyCollect> specificationBuilder = builder.init(paramObject)
                .sEqual("type", "type")
                .sEqual("userId", "userId")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");
        return specificationBuilder.build();
    }
}
