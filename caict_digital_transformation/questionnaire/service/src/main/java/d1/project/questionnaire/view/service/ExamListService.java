package d1.project.questionnaire.view.service;

import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.util.SpecificationBuilder;
import d1.project.questionnaire.view.dao.ExamListDao;
import d1.project.questionnaire.view.entity.ExamList;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author baozh
 */
@Service
public class ExamListService {
    private final ExamListDao examListDao;

    public ExamListService(ExamListDao examListDao) {
        this.examListDao = examListDao;
    }

    public Page<ExamList> findAllByParams(JSONObject json) throws Exception {
        SpecificationBuilder<ExamList> builder = new SpecificationBuilder<>();
        Specification<ExamList> build = builder.init(json)
                .sContain("name", "name")
                .dOrder("createTime").build();
        return examListDao.findAll(build, builder.getPageable());
    }
}
