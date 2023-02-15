package d1.project.caict.business.service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.business.dao.QuestionnaireDao;
import d1.project.caict.business.entity.Questionnaires;
import d1.project.caict.business.model.QuestionnaireFindAllVm;
import d1.project.caict.business.model.QuestionnaireInsertVm;
import d1.project.caict.business.model.QuestionnaireUpdateVm;
import d1.project.caict.business.utils.Utils;
import d1.project.caict.common.Constants;
import d1.project.caict.common.utils.BaseUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
@Service
public class QuestionnaireService {

    private final QuestionnaireDao questionnaireDao;

    public QuestionnaireService(QuestionnaireDao questionnaireDao) {
        this.questionnaireDao = questionnaireDao;
    }

    public Page<Questionnaires> findAll(QuestionnaireFindAllVm model) throws Exception {
        JSONObject paramObject = (JSONObject) JSON.toJSON(model);
        SpecificationBuilder<Questionnaires> builder = new SpecificationBuilder<>();
        return questionnaireDao.findAll(getSpecification(paramObject, builder), builder.getPageable());
    }


    public Optional<Questionnaires> find(String id) {
        return questionnaireDao.findById(id);
    }

    public void delete(String id) {
        this.questionnaireDao.deleteById(id);
    }

    public void save(QuestionnaireInsertVm model, HttpServletRequest request) throws DoValidException {
        Questionnaires entity = new Questionnaires();
        BeanUtils.copyProperties(model, entity);

        entity.setId(BaseUtils.generate32Id());
        entity.setCreateTime(Calendar.getInstance());
        entity.setCreateById(Utils.getCurrentUserId(request));
        this.questionnaireDao.save(entity);
    }

    public void update(QuestionnaireUpdateVm model, HttpServletRequest request) throws DoValidException {
        Questionnaires entity = questionnaireDao.findById(model.getId()).orElseThrow(() -> new DoValidException("调查问卷不存在"));
        BeanUtils.copyProperties(model, entity);

        entity.setUpdateTime(Calendar.getInstance());
        entity.setUpdateById(Utils.getCurrentUserId(request));
        this.questionnaireDao.save(entity);
    }

    ////////////////////////////////////////////////////////

    /**
     * 获取查询规则
     */
    private Specification<Questionnaires> getSpecification(JSONObject paramObject, SpecificationBuilder<Questionnaires> builder) throws Exception {
        SpecificationBuilder<Questionnaires> specificationBuilder = builder.init(paramObject)
                .sContain("name", "name")
                .sEqual("type", "type")
                .sEqual("userId", "userId")
                .tBetween("createTime", "startTime", "endTime", Constants.DATE_TIME_FORMAT)
                .dOrder("createTime");
        return specificationBuilder.build();
    }

}
