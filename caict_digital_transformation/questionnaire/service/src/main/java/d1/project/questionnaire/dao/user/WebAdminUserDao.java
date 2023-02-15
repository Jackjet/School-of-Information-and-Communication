package d1.project.questionnaire.dao.user;

import d1.project.questionnaire.entity.user.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * @author Kikki 2019/8/28 17:08
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String> {


    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return d1.project.liangxin.entity.user.AppUser
     * @author Kikki 2019/8/28 17:09
     **/
    WebAdminUser findByName(String name);

    /**
     * 验证用户名是否存在
     *
     * @param name 用户名
     * @return d1.project.liangxin.entity.user.AppUser
     * @author Kikki 2019/8/28 17:09
     **/
    boolean existsByName(String name);

    /**
     * 验证用户名或手机号是否存在
     *
     * @param phone 手机号
     * @param name  用户名
     * @return boolean
     * @author Kikki 2019/8/28 17:10
     **/
    boolean existsByPhoneOrName(String phone, String name);

    /**
     * 通过姓名或电话找到其他用户
     *
     * @param phone 手机号
     * @param name  用户名
     * @param id    用户id
     * @return int
     * @author Kikki 2019/8/28 17:11
     **/
    @Query(value = "select count(*) from d1_app_user where (phone=?1 or name=?2) and id!=?3", nativeQuery = true)
    int findOtherUserByNameOrPhone(String phone, String name, String id);

}
