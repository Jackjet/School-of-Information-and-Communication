package d1.project.questionnaire.dao.user;

import d1.project.questionnaire.entity.user.AppUserCourse;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-16 10:05
 */
public interface AppUserCourseDao extends JpaRepository<AppUserCourse, String>, JpaSpecificationExecutor<AppUserCourse> {

    Optional<AppUserCourse> findByUserId(String userId);

}
