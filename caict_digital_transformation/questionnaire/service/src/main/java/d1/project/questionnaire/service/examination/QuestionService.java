package d1.project.questionnaire.service.examination;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.QuestionDao;
import d1.project.questionnaire.entity.examination.Question;
import d1.project.questionnaire.util.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:45
 */

@Service
public class QuestionService extends DoServiceImpBase<Question> {
    private final QuestionDao questionDao;

    public QuestionService(QuestionDao questionDao) {
        this.questionDao = questionDao;
    }

    @Override
    protected JpaRepository<Question, String> getDao() {
        return questionDao;
    }

    public boolean existsAllByExaminationGroupId(String groupId) {
        return questionDao.existsAllByExaminationGroupId(groupId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByIdIn(List<String> ids) {
        questionDao.deleteAllByIdIn(ids);
    }

    @Transactional(rollbackFor = Exception.class)
    public void updateGroupIdByIds(String groupId, List<String> ids) {
        questionDao.updateGroupIdByIds(groupId, ids);
    }

    public Page<Question> findByParams(JSONObject params) throws Exception {
        SpecificationBuilder<Question> builder = new SpecificationBuilder<>();
        Specification<Question> build = builder.init(params)
                .sContain("topic", "topic")
                .sEqual("type", "type")
                .sEqual("examinationGroupId", "examinationGroupId")
                .dOrder("createTime").build();
        return questionDao.findAll(build, builder.getPageable());
    }

    public List<Question> findQuestionListByIds(List<String> ids){
        return questionDao.findAllByIdInOrderByCreateTimeDesc(ids);
    }

    public Optional<Question> getOptional(Function<QuestionDao, Optional<Question>> daoLongFunction) {
        return daoLongFunction.apply(questionDao);
    }

    public List<Question> getList(Function<QuestionDao, List<Question>> daoLongFunction) {
        return daoLongFunction.apply(questionDao);
    }

}
