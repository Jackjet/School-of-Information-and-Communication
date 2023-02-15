package d1.project.cms.service.template;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.Constants;
import d1.project.cms.dao.template.DataFieldDao;
import d1.project.cms.entity.template.DataField;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

/**
 * @author baozh
 */
@Service
public class DataFieldService {
    private final DataFieldDao dataFieldDao;

    public DataFieldService(DataFieldDao dataFieldDao) {
        this.dataFieldDao = dataFieldDao;
    }

    public Page<DataField> find(JSONObject params) throws Exception{
        SpecificationBuilder<DataField> builder = new SpecificationBuilder<>();
        Specification<DataField> specification = builder.init(params)
                .sEqual("type", "type")
                .sEqual("templateId","templateId")
                .aOrder("createTime").build();
        return dataFieldDao.findAll(specification,builder.getPageable());
    }

    public DataField findById(String id) throws Exception{
        Optional<DataField> temp = dataFieldDao.findById(id);
        if (!temp.isPresent()){
            throw new DoValidException(Constants.DATA_IS_NULL);
        }
        return temp.get();
    }

    public void insert(DataField dataField) throws Exception{
        String templateId = dataField.getTemplateId();
        String field = dataField.getField();
        String type = dataField.getType();
        if (dataFieldDao.existsAllByTemplateIdAndFieldAndType(templateId,field,type)){
            throw new DoValidException(Constants.DATA_FIELD_DATA_REPEAT);
        }
        dataFieldDao.save(dataField);
    }

    public void update(DataField dataField) throws Exception{
        String id = dataField.getId();
        String templateId = dataField.getTemplateId();
        String field = dataField.getField();
        String type = dataField.getType();
        if (dataFieldDao.existsAllByTemplateIdAndFieldAndTypeAndIdNot(templateId,field,type,id)){
            throw new DoValidException(Constants.DATA_FIELD_DATA_REPEAT);
        }
        dataFieldDao.save(dataField);
    }

    public void deleteByTemplateId(String templateId){
        dataFieldDao.deleteAllByTemplateId(templateId);
    }

    public void delete(String id){
        dataFieldDao.deleteById(id);
    }

    public List<DataField> findAllByTemplateIdAndType(String templateId,String type){
        return this.dataFieldDao.findAllByTemplateIdAndType(templateId,type);
    }

    public boolean existsByTemplateId(String templateId){
        return dataFieldDao.existsByTemplateId(templateId);
    }
}
