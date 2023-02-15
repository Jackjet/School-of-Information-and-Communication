package d1.project.questionnaire.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.util.SpecificationBuilder;
import d1.project.questionnaire.view.dao.ExamGroupNumDao;
import d1.project.questionnaire.view.entity.ExamGroupNum;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author baozh
 */
@Service
public class ExamGroupNumService {
    private final ExamGroupNumDao examGroupNumDao;

    public ExamGroupNumService(ExamGroupNumDao examGroupNumDao) {
        this.examGroupNumDao = examGroupNumDao;
    }

    public List<ExamGroupNum> findAllByParams(JSONObject json) throws Exception {
        SpecificationBuilder<ExamGroupNum> builder = new SpecificationBuilder<>();
        Specification<ExamGroupNum> build = builder.init(json)
                .sContain("name", "name")
                .aOrder("createTime").build();
        return examGroupNumDao.findAll(build);
    }
}
