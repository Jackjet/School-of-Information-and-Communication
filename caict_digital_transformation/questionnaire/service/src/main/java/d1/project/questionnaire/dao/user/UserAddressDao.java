package d1.project.questionnaire.dao.user;

import d1.project.questionnaire.entity.user.UserAddress;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-15 16:23
 */
public interface UserAddressDao extends JpaRepository<UserAddress, String>, JpaSpecificationExecutor<UserAddress> {
    void deleteByUserId(String userId);

    Optional<UserAddress> findByUserId(String userId);
}
