package d1.project.cms.service.homepage;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.dao.homepage.QuickNavigationDao;
import d1.project.cms.entity.homepage.QuickNavigation;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class QuickNavigationService {
    private final QuickNavigationDao navigationDao;

    public QuickNavigationService(QuickNavigationDao navigationDao) {
        this.navigationDao = navigationDao;
    }

    public List<QuickNavigation> find(JSONObject params) throws Exception {
        SpecificationBuilder<QuickNavigation> builder = new SpecificationBuilder<>();
        Specification<QuickNavigation> specification = builder.init(params)
                .sEqual("userId", "userId")
                .aOrder("createTime").build();
        return navigationDao.findAll(specification);
    }

    public void insert(QuickNavigation navigation){
        navigationDao.saveAndFlush(navigation);
    }

    public void deleteById(String id){
        navigationDao.deleteById(id);
    }

    public void deleteByUserId(String userId){
        navigationDao.deleteByUserId(userId);
    }
}
