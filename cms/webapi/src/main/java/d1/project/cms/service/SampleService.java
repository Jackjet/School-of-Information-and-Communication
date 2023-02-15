package d1.project.cms.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.dao.SampleDao;
import d1.project.cms.entity.SampleEntity;
import d1.project.cms.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

/**
 * 针对webadmin的登录等操作
 *
 * @author Buter
 * @date 2020/3/16 6:50
 */
@Service
public class SampleService {
    private final SampleDao sampleDao;
    private final TokenService tokenService;

    @Autowired
    public SampleService(SampleDao sampleDao, TokenService tokenService) {
        this.sampleDao = sampleDao;
        this.tokenService = tokenService;
    }

    public Page<SampleEntity> findAll(JSONObject params) throws Exception {
        params.put("sContainField", "ab");
        JSONArray array = new JSONArray();
        array.add("aaa");
        array.add("bbb");
        array.add("ccc");
        params.put("array", array);
        String dateFormat = "yyyy-MM-dd HH:mm:ss";

        SpecificationBuilder<SampleEntity> builder = new SpecificationBuilder<>();
        Specification<SampleEntity> specification = builder.init(params)
                .nLess("iField", "iField")
                .sEqual("sField", "sField")
                //.sIn("sField", "array")
                //.sContain("sField", "sContainField")
                .tBetween("tField", "stField", "etField", dateFormat)
                .dOrder("createTime").build();

        return this.sampleDao.findAll(specification, builder.getPageable());
    }

    @Transactional(rollbackFor = Exception.class)
    public void insert(JSONObject params, HttpServletRequest request){
        SampleEntity entity = JSON.toJavaObject(params, SampleEntity.class);
        entity.setId(MyUtils.generate32Id());
        tokenService.updateCreateIdAndTime(request, entity);
        sampleDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        if (StringUtils.isEmpty(id)) {
            throw new DoValidException("id不能为空");
        }
        Optional<SampleEntity> entityOptional = sampleDao.findById(id);
        if (!entityOptional.isPresent()) {
            throw new Exception("找不到记录：" + id);
        }
        SampleEntity entity = entityOptional.get();
        entity.setiField(params.getInteger("iField"));
        entity.setsField(params.getString("sField"));
        tokenService.updateUpdateIdAndTime(request, entity);
        sampleDao.save(entity);
    }

    @Transactional(rollbackFor = Exception.class)
    public void delete(String id) throws Exception {
        if (StringUtils.isEmpty(id)) {
            throw new DoValidException("id不能为空");
        }
        Optional<SampleEntity> entityOptional = sampleDao.findById(id);
        if (!entityOptional.isPresent()) {
            throw new Exception("找不到记录：" + id);
        }
        sampleDao.delete(entityOptional.get());
    }
}
