package d1.project.questionnaire.service.examination;

import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.ExaminationUserDao;
import d1.project.questionnaire.entity.examination.ExaminationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Function;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:54
 */
@Service
public class ExaminationUserService extends DoServiceImpBase<ExaminationUser> {
    final
    ExaminationUserDao examinationUserDao;

    public ExaminationUserService(ExaminationUserDao examinationUserDao) {
        this.examinationUserDao = examinationUserDao;
    }

    @Override
    protected void beforeInsert(ExaminationUser opt) throws Exception {
        super.beforeInsert(opt);
        if (examinationUserDao.existsByUserIdAndLearnPlanIdAndExaminationId(opt.getUserId(), opt.getLearnPlanId(), opt.getExaminationId())) {
            throw new Exception("重复交卷");
        }
    }

    public Long getLong(Function<ExaminationUserDao, Long> daoLongFunction) {
        return daoLongFunction.apply(examinationUserDao);
    }

    public Optional<ExaminationUser> getOptional(Function<ExaminationUserDao, Optional<ExaminationUser>> daoLongFunction) {
        return daoLongFunction.apply(examinationUserDao);
    }


    @Override
    protected JpaRepository<ExaminationUser, String> getDao() {
        return examinationUserDao;
    }
}
