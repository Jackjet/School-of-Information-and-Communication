package d1.project.questionnaire.dao.user;

import d1.project.questionnaire.entity.user.AppUser;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author Kikki 2019/8/28 17:08
 */
public interface AppUserDao extends JpaRepository<AppUser, String> {

    /**
     * 根据用户名查询用户
     *
     * @param name 用户名
     * @return d1.project.liangxin.entity.user.AppUser
     * @author Kikki 2019/8/28 17:09
     **/
    AppUser findByName(String name);

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
     * @param id    用户id
     * @return int
     * @author Kikki 2019/8/28 17:11
     **/
    @Query(value = "select count(*) from d1_app_user where phone=?1 and id!=?2", nativeQuery = true)
    int findOtherUserByNameOrPhone(String phone, String id);

    /**
     * 根据推送状态查询
     *
     * @param isPush 推送状态
     * @return 查询结果
     */
    @Query(value = "SELECT id FROM d1_app_user WHERE is_push = ?1 ", nativeQuery = true)
    List<String> findByPush(String isPush);

    /**
     * 根据用户名查询用户
     *
     * @param phone 用户手机号
     * @return d1.project.liangxin.entity.user.AppUser
     * @author Kikki 2019/8/28 17:09
     **/
    AppUser findByPhone(String phone);

    /**
     * 根据额外信息1查询用户(微信ID)
     *
     * @param weChatId 额外信息1
     * @return 用户信息
     */
    AppUser findByWeChatId(String weChatId);

    /**
     * 额外信息1是否存在数据
     *
     * @param weChatId 额外信息1
     * @return Boolean
     */
    boolean existsByWeChatId(String weChatId);

    /**
     * 根据手机号查询是否存在该用户
     *
     * @param phone 手机号
     * @return Boolean
     */
    boolean existsByPhone(String phone);

    /**
     * 根据用户ID查询返回用户信息（同时 查询用户评论数，收藏 数，笔记数，卜卦记录数）
     *
     * @param userId 用户ID
     * @return 查询结果
     */
    @Query(value =
            "SELECT d1_app_user.* ," +
                    "table1.num AS collectNum," +
                    "table2.num AS noteNum," +
                    "table3.num AS commentNum," +
                    "table4.num AS recordNum " +
                    "FROM d1_app_user " +
                    "LEFT JOIN " +
                    "(SELECT create_by_id as id,COUNT(id) as num FROM d1_collect WHERE type IN ('2','6','8','9') GROUP BY create_by_id )as table1 " +
                    "ON d1_app_user.id = table1.id " +
                    "LEFT JOIN " +
                    "(SELECT create_by_id as id,COUNT(id) as num FROM d1_note GROUP BY create_by_id) as table2 " +
                    "ON d1_app_user.id = table2.id " +
                    "LEFT JOIN " +
                    "(SELECT create_by_id as id,COUNT(id) as num FROM d1_comments GROUP BY create_by_id) as table3 " +
                    "ON d1_app_user.id = table3.id " +
                    "LEFT JOIN " +
                    "(SELECT create_by_id as id,COUNT(id) as num FROM d1_hexagram_record GROUP BY create_by_id) AS table4 " +
                    "ON d1_app_user.id = table4.id " +
                    "WHERE d1_app_user.id = ?1", nativeQuery = true)
    Map<String, Object> findByUserId(String userId);

    /**
     * 统计性别
     *
     * @return 查询结果
     */
    @Query(value = "SELECT CASE sex " +
            " WHEN 0 THEN '男' " +
            " WHEN 1 THEN '女' " +
            " ELSE '未知' END " +
            " AS name,COUNT(sex) as value FROM d1_app_user GROUP BY sex", nativeQuery = true)
    List<Map<String, Object>> groupBySex();


    /**
     * 查询距今天 多少天 的统计信息信息
     *
     * @param dayNum 天数
     * @return 查询结果
     */
    @Query(value =
            "SELECT DATE_FORMAT(daytable.date, '%Y-%m-%d') as name,IFNULL(maintable.value ,0)as value FROM " +
                    "(" +
                    "SELECT @s \\:=@s + 1 as `index`, DATE(DATE_SUB(CURRENT_DATE, INTERVAL @s DAY)) AS `date` " +
                    "FROM mysql.help_topic,(SELECT @s \\:= -1) temp WHERE @s < ?1 ORDER BY `date` " +
                    ") as daytable " +
                    "LEFT JOIN " +
                    "(" +
                    "SELECT table1.type as name ,SUM(table1.num) as value FROM " +
                    "(SELECT DATE_FORMAT(sign_up_time, '%Y-%m-%d') as type ," +
                    "COUNT(sign_up_time) AS num FROM d1_app_user " +
                    "WHERE sign_up_time BETWEEN date_add(now(), interval - ?1 day) AND NOW() GROUP BY sign_up_time ORDER BY type " +
                    ")as table1 GROUP BY table1.type " +
                    ") as maintable  " +
                    "ON daytable.date = maintable.name " +
                    "ORDER BY daytable.date", nativeQuery = true)
    List<Map<String, Object>> findByDay(Integer dayNum);

    /**
     * 统计当前时间前一天的注册数量
     *
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*) FROM d1_app_user " +
            "WHERE sign_up_time BETWEEN DATE_ADD(DATE_SUB(CURDATE(),INTERVAL 1 DAY),INTERVAL 0 HOUR) " +
            "AND DATE_SUB(CURDATE(),INTERVAL 0 HOUR) ", nativeQuery = true)
    Long countYesterday();

    /**
     * 查询 所有用户
     *
     * @param name     用户名
     * @param phone    手机号
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query(value = "SELECT d1_app_user.* ," +
            "table2.num AS noteNum," +
            "table3.num AS commentNum," +
            "table4.num AS recordNum " +
            "FROM d1_app_user " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_note GROUP BY create_by_id) as table2 " +
            "ON d1_app_user.id = table2.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_comments GROUP BY create_by_id) as table3 " +
            "ON d1_app_user.id = table3.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_hexagram_record GROUP BY create_by_id) AS table4 " +
            "ON d1_app_user.id = table4.id " +
            "WHERE IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) " +
            "ORDER BY d1_app_user.last_sign_in_time DESC ", nativeQuery = true)
    List<Map<String, Object>> findAllUser(String name, String phone, Pageable pageable);

    /**
     * 查询 所有用户
     *
     * @param name     用户名
     * @param phone    手机号
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query(value = "SELECT d1_app_user.* ," +
            "table2.num AS noteNum," +
            "table3.num AS commentNum," +
            "table4.num AS recordNum " +
            "FROM d1_app_user " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_note GROUP BY create_by_id) as table2 " +
            "ON d1_app_user.id = table2.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_comments GROUP BY create_by_id) as table3 " +
            "ON d1_app_user.id = table3.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_hexagram_record GROUP BY create_by_id) AS table4 " +
            "ON d1_app_user.id = table4.id " +
            "WHERE IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) " +
            "AND  d1_app_user.is_black = '1'" +
            "ORDER BY d1_app_user.last_sign_in_time DESC ", nativeQuery = true)
    List<Map<String, Object>> findAllUserisBlack(String name, String phone, Pageable pageable);
    /**
     * 查询 所有用户黑名单用户
     *
     * @param name  用户名
     * @param phone 手机号
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM d1_app_user " +
            "WHERE IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) " +
            "AND  d1_app_user.is_black = '1'", nativeQuery = true)
    Long countAllUserisBlack(String name, String phone);

    /**
     * 查询 所有用户黑名单用户数量
     *
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM d1_app_user " +
            "WHERE d1_app_user.is_black = '1'", nativeQuery = true)
    Long countIsBlack();
    /**
     * 查询 黑名单
     *
     * @param name  用户名
     * @param phone 手机号
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*) " +
            "FROM d1_app_user " +
            "WHERE IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) ", nativeQuery = true)
    Long countAllUser(String name, String phone);


    /**
     * 查询昨天新增 所有用户
     *
     * @param name     用户名
     * @param phone    手机号
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query(value = "SELECT d1_app_user.* ," +
            "table2.num AS noteNum," +
            "table3.num AS commentNum," +
            "table4.num AS recordNum " +
            "FROM d1_app_user " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_note GROUP BY create_by_id) as table2 " +
            "ON d1_app_user.id = table2.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_comments GROUP BY create_by_id) as table3 " +
            "ON d1_app_user.id = table3.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_hexagram_record GROUP BY create_by_id) AS table4 " +
            "ON d1_app_user.id = table4.id " +
            "WHERE d1_app_user.sign_up_time BETWEEN DATE_ADD(DATE_SUB(CURDATE(),INTERVAL 1 DAY),INTERVAL 0 HOUR) " +
            "AND DATE_SUB(CURDATE(),INTERVAL 0 HOUR) " +
            "AND IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) " +
            "ORDER BY d1_app_user.last_sign_in_time DESC ", nativeQuery = true)
    List<Map<String, Object>> findYesterdayUser(String name, String phone, Pageable pageable);

    /**
     * 统计当前时间前一天的注册数量
     *
     * @param name  用户名
     * @param phone 手机号
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*) FROM d1_app_user " +
            "WHERE sign_up_time BETWEEN DATE_ADD(DATE_SUB(CURDATE(),INTERVAL 1 DAY),INTERVAL 0 HOUR) " +
            "AND DATE_SUB(CURDATE(),INTERVAL 0 HOUR) " +
            "AND IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) ", nativeQuery = true)
    Long countYesterdayUser(String name, String phone);

    /**
     * 查询上周所有用户
     *
     * @param name     用户名
     * @param phone    手机号
     * @param pageable 分页信息
     * @return 查询结果
     */
    @Query(value = "SELECT d1_app_user.* ," +
            "table2.num AS noteNum," +
            "table3.num AS commentNum," +
            "table4.num AS recordNum " +
            "FROM d1_app_user " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_note GROUP BY create_by_id) as table2 " +
            "ON d1_app_user.id = table2.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_comments GROUP BY create_by_id) as table3 " +
            "ON d1_app_user.id = table3.id " +
            "LEFT JOIN " +
            "(SELECT create_by_id as id,COUNT(id) as num FROM d1_hexagram_record GROUP BY create_by_id) AS table4 " +
            "ON d1_app_user.id = table4.id " +
            "WHERE d1_app_user.id IN " +
            "(SELECT create_by_id FROM d1_login_record " +
            "WHERE create_time  " +
            "BETWEEN DATE_FORMAT( DATE_SUB( DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 1 WEEK), '%Y-%m-%d 00:00:00') " +
            "AND DATE_FORMAT( SUBDATE(CURDATE(), WEEKDAY(CURDATE()) + 1), '%Y-%m-%d 23:59:59'))" +
            "AND IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) " +
            "ORDER BY d1_app_user.last_sign_in_time DESC ", nativeQuery = true)
    List<Map<String, Object>> findLastWeekUser(String name, String phone, Pageable pageable);

    /**
     * 查询上周所有用户
     *
     * @param name  用户名
     * @param phone 手机号
     * @return 查询结果
     */
    @Query(value = "SELECT COUNT(*)  " +
            "FROM d1_app_user " +
            "WHERE d1_app_user.id IN " +
            "(SELECT create_by_id FROM d1_login_record " +
            "WHERE create_time  " +
            "BETWEEN DATE_FORMAT( DATE_SUB( DATE_SUB(CURDATE(), INTERVAL WEEKDAY(CURDATE()) DAY), INTERVAL 1 WEEK), '%Y-%m-%d 00:00:00') " +
            "AND DATE_FORMAT( SUBDATE(CURDATE(), WEEKDAY(CURDATE()) + 1), '%Y-%m-%d 23:59:59'))" +
            "AND IF(?1 != '' , d1_app_user.name LIKE CONCAT('%',?1,'%'),1=1) " +
            "AND IF(?2 != '' , d1_app_user.phone LIKE CONCAT('%',?2,'%'),1=1) ", nativeQuery = true)
    Long countLastWeekUser(String name, String phone);

    @Query(value =
            "SELECT * FROM d1_app_user WHERE phone !='' AND ISNULL(open_user_id)",nativeQuery = true)
    List<AppUser> findAllUserWithOut();
}
