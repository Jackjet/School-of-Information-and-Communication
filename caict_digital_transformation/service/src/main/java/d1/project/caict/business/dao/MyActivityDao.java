package d1.project.caict.business.dao;

import d1.project.caict.business.entity.MyActivities;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface MyActivityDao extends JpaRepository<MyActivities, String>, JpaSpecificationExecutor<MyActivities> {

    /**
     * 查询所有参与的用户
     *
     * @param activityId 活动id
     * @return 用户列表
     */
    List<MyActivities> findAllByActivityId(String activityId);

    /**
     * 查询用户是否参加该活动
     */
    Optional<MyActivities> findFirstByActivityIdAndUserId(String activityId, String userId);

    /**
     * 查询我的参加的活动列表
     */
    <T> Page<T> execSqlWithPage(@NotNull String sql, @NotNull Pageable pageable, @NotNull Class<T> clazz);

}
