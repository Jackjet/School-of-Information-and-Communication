package d1.project.questionnaire.service.user;

import d1.framework.webapi.service.impl.DoUserServiceImplBase;
import d1.project.questionnaire.dao.user.AppUserCourseDao;
import d1.project.questionnaire.entity.user.AppUserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.UnaryOperator;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-16 10:05
 */
@Service
public class AppUserCourseService extends DoUserServiceImplBase<AppUserCourse> {
    final
    AppUserCourseDao appUserCourseDao;

    public AppUserCourseService(AppUserCourseDao appUserCourseDao) {
        this.appUserCourseDao = appUserCourseDao;
    }

    public AppUserCourse insert(UnaryOperator<AppUserCourse> opt) throws Exception {
        AppUserCourse userIntegral = new AppUserCourse();
        super.insert(opt.apply(userIntegral));
        return userIntegral;
    }

    public long count() {
        return appUserCourseDao.count();
    }

    public Optional<AppUserCourse> findByIdMy(String id) {
        return appUserCourseDao.findById(id);
    }

    public Optional<AppUserCourse> findByUserId(String userId) {
        return appUserCourseDao.findByUserId(userId);
    }

    @Override
    protected JpaRepository<AppUserCourse, String> getDao() {
        return appUserCourseDao;
    }
}
