package d1.project.cms.service.template;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.Constants;
import d1.project.cms.dao.template.TemplateDao;
import d1.project.cms.entity.homepage.QuickNavigation;
import d1.project.cms.entity.template.Template;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class TemplateService {
    private final TemplateDao templateDao;

    public TemplateService(TemplateDao templateDao) {
        this.templateDao = templateDao;
    }

    /**
     * 根据模板名查询
     * @param params 查询条件
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<Template> find(JSONObject params) throws Exception{
        SpecificationBuilder<Template> builder = new SpecificationBuilder<>();
        Specification<Template> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime").build();
        return templateDao.findAll(specification,builder.getPageable());
    }

    public Template findById(String id) throws DoValidException {
        Optional<Template> temp = templateDao.findById(id);
        if (!temp.isPresent()){
            throw new DoValidException(Constants.DATA_IS_NULL);
        }
        return temp.get();
    }

    public void insert(Template template) throws Exception{
        String name = template.getName();
        if (templateDao.existsAllByName(name)){
            throw new Exception(Constants.TEMPLATE_DATA_REPEAT);
        }
        templateDao.save(template);
    }

    public void update(Template template) throws Exception{
        String name = template.getName();
        String id = template.getId();
        if (templateDao.existsAllByNameAndIdNot(name,id)){
            throw new Exception(Constants.TEMPLATE_DATA_REPEAT);
        }
        templateDao.save(template);
    }

    public void delete(String id){
        templateDao.deleteById(id);
    }

    public List<Template> templateFindAll(){
        return templateDao.findAll();
    }

    public Template getTemplate(String id){
        return templateDao.findById(id).orElse(null);
    }

    public List<String> findAllByIsPcListOrIsAppList(String isPcList,String isAppList){
        return templateDao.findAllByIsPcListOrIsAppList(isPcList,isAppList);
    }
}
