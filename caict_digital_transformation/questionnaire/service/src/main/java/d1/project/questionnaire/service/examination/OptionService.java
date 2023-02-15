package d1.project.questionnaire.service.examination;


import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.OptionDao;
import d1.project.questionnaire.entity.examination.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collection;
import java.util.List;
import java.util.function.Function;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:47
 */
@Service
public class OptionService extends DoServiceImpBase<Option> {
    private final OptionDao optionDao;

    public OptionService(OptionDao optionDao) {
        this.optionDao = optionDao;
    }

    @Override
    protected JpaRepository<Option, String> getDao() {
        return optionDao;
    }

    public List<Option> findAllByQuestionId(String questionId) {
        return optionDao.findAllByQuestionIdOrderByNumberAsc(questionId);
    }

    @Transactional(rollbackFor = Exception.class)
    public void deleteByQuestionIdIn(List<String> ids) {
        optionDao.deleteAllByQuestionIdIn(ids);
    }


    public  Object[]  findAllByQuestionAnswer(Collection<String> questionIds) {
        return optionDao.findAllByQuestionAnswer(questionIds);
    }

    public List<Option> getList(Function<OptionDao, List<Option>> daoLongFunction) {
        return daoLongFunction.apply(optionDao);
    }
}
