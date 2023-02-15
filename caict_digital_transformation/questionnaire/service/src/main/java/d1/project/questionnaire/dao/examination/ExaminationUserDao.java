package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.ExaminationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:54
 */
public interface ExaminationUserDao extends JpaRepository<ExaminationUser, String>, JpaSpecificationExecutor<ExaminationUser> {

    long countByLearnPlanIdAndCampPeriodId(String learnPlanId, String campPeriodId);

    long countByLearnPlanIdAndTrainingCampId(String learnPlanId, String trainingCampId);

    List<ExaminationUser> findByLearnPlanIdAndCampPeriodId(String learnPlanId, String campPeriodId);

    List<ExaminationUser> findByLearnPlanIdAndTrainingCampId(String learnPlanId, String trainingCampId);

    @Query(value = "select ifnull(sum(user_allfraction), 0)\n" +
            "        from d1_examination_user  where learn_plan_id =?1 and camp_period_id =?2 ", nativeQuery = true)
    long sumUserAllFractionByLearnPlanIdAndCampPeriodId(String learnPlanId, String campPeriodId);

    long countByCampPeriodIdAndLearnPlanIdAndUserAllfractionLessThanAndUserIdNot(String campPeriodId, String learnPlanId, Integer userAllfraction, String userId);

    boolean existsByUserIdAndLearnPlanIdAndExaminationId(String userId, String learnPlanId, String examinationId);

    Optional<ExaminationUser> findByLearnPlanIdAndUserId(String learnPlanId, String userId);

    Optional<ExaminationUser> findByLearnPlanIdAndCampPeriodIdAndUserId(String learnPlanId, String campPeriodId, String userId);
}
