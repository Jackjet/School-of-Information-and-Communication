package d1.project.caict.business.dao;

import d1.project.caict.business.entity.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;
import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface UserDao extends JpaRepository<Users, String>, JpaSpecificationExecutor<Users> {

    /**
     * 根据用户名/手机号查找用户
     *
     * @param username 用户名
     * @param phone    手机号
     * @return 用户
     */
    Optional<Users> findByUsernameOrPhone(String username, String phone);

    /**
     * 根据手机号查找用户
     *
     * @param phone 手机号
     * @return 用户
     */
    Optional<Users> findByPhone(String phone);

    /**
     * 统计新增人数
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     */
    @Query(value = "SELECT regist_day, count(regist_day) FROM d1_users where regist_day BETWEEN ?1 AND ?2 GROUP BY regist_day ORDER BY regist_day DESC", nativeQuery = true)
    List<Map<String, String>> statisticNewUser(String startTime, String endTime);

    /**
     * 统计用户来源
     */
    @Query(value = "SELECT source, count(source) FROM d1_users GROUP BY source", nativeQuery = true)
    List<Map<String, String>> statisticUserSource();

    /**
     * 统计用户地区
     */
    @Query(value = "SELECT province, count(province) FROM d1_users WhERE province is NOT NULL GROUP BY province", nativeQuery = true)
    List<Map<String, String>> statisticUserAddress();

}
