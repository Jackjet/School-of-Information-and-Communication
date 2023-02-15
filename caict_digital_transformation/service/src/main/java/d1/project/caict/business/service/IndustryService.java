package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.IndustryDao;
import d1.project.caict.business.entity.Industries;
import d1.project.caict.business.model.IndustryFindAllVm;
import d1.project.caict.business.model.IndustryInsertVm;
import d1.project.caict.business.model.IndustryUpdateVm;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class IndustryService {

    private final IndustryDao industryDao;

    public IndustryService(IndustryDao industryDao) {
        this.industryDao = industryDao;
    }

    public Page<Industries> findAll(IndustryFindAllVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<Industries> builder = new SpecificationBuilder<>();
        return industryDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }


    /**
     * 解决 客户列表 显示所属行业 id 的问题
     * 通过一次性查询所有的行业信息
     */
    public Map<String, String> findAllNoPage() {
        List<Map<String, String>> list = industryDao.findAllNoPage();
        Map<String, String> result = new HashMap<>();
        for (Map<String, String> map : list) {
            result.put(map.get("id"), map.get("name"));
        }
        return result;
    }

    public Optional<Industries> find(String id) {
        return industryDao.findById(id);
    }

    public void delete(String id) {
        this.industryDao.deleteById(id);
    }

    public void save(IndustryInsertVm model, HttpServletRequest request) throws DoValidException {
        Industries entity = new Industries();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        this.industryDao.save(entity);
    }

    public void update(IndustryUpdateVm model, HttpServletRequest request) throws DoValidException {
        Industries entity = industryDao.findById(model.getId()).orElseThrow(() -> new DoValidException("行业信息不存在"));
        BeanUtils.copyProperties(model, entity);

        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        this.industryDao.save(entity);
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<Industries> getSpecification(JSONObject paramObject, SpecificationBuilder<Industries> builder) throws Exception {
        SpecificationBuilder<Industries> specificationBuilder = builder.init(paramObject)
                .sContain("name", "name")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");
        return specificationBuilder.build();
    }

}
