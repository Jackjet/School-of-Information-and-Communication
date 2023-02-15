package d1.project.questionnaire.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.util.SpecificationBuilder;
import d1.project.questionnaire.view.dao.QuestionBankDao;
import d1.project.questionnaire.view.entity.QuestionBank;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class QuestionBankService {
    private final QuestionBankDao questionBankDao;

    public QuestionBankService(QuestionBankDao questionBankDao) {
        this.questionBankDao = questionBankDao;
    }

    public Page<QuestionBank> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<QuestionBank> builder = new SpecificationBuilder<>();
        Specification<QuestionBank> build = builder.init(params)
                .sContain("topic", "topic")
                .sEqual("type", "type")
                .sEqual("examinationGroupId", "examinationGroupId")
                .dOrder("updateTime").build();
        return questionBankDao.findAll(build, builder.getPageable());
    }
}
