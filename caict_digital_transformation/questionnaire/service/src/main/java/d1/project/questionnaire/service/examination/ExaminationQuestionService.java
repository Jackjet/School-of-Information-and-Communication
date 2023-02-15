package d1.project.questionnaire.service.examination;

import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.ExaminationQuestionDao;
import d1.project.questionnaire.entity.examination.ExaminationQuestion;
import d1.project.questionnaire.model.exam.examinationquestion.ExaminationQuestionInsertVm;
import d1.project.questionnaire.util.BaseUtils;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:53
 */
@Service
public class ExaminationQuestionService extends DoServiceImpBase<ExaminationQuestion> {

    final
    ExaminationQuestionDao examinationQuestionDao;

    public ExaminationQuestionService(ExaminationQuestionDao examinationQuestionDao) {
        this.examinationQuestionDao = examinationQuestionDao;
    }

    @Override
    protected JpaRepository<ExaminationQuestion, String> getDao() {
        return examinationQuestionDao;
    }

    public ExaminationQuestion insert(ExaminationQuestionInsertVm insertVm, HttpServletRequest request) throws Exception {
        String userId = getCurrentUser(request).getString("id");
        ExaminationQuestion audio = BaseUtils.copyPropertiesInsert(insertVm, userId, ExaminationQuestion.class);
        insert(audio);
        return audio;
    }

    public List<ExaminationQuestion> findByExamId(String examId) {
        return examinationQuestionDao.findAllByExaminationIdOrderByNumberAsc(examId);
    }
    public List<ExaminationQuestion> findByExaminationIdAndQuestionIdIn(String examinationId, Collection<String> questionId) {
        return examinationQuestionDao.findByExaminationIdAndQuestionIdIn(examinationId, questionId);
    }
    @Transactional(rollbackFor = Exception.class)
    public void deleteAllByExamId(String examId) {
        examinationQuestionDao.deleteAllByExaminationId(examId);
    }
    public boolean existsAllByQuestionIdIn(List<String> ids){
       return examinationQuestionDao.existsAllByQuestionIdIn(ids);
    }
}
