package d1.project.caict.system.dao;

import d1.project.caict.system.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-07 15:21
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String>, JpaSpecificationExecutor<WebAdminUser> {

    /**
     * 查找账户
     *
     * @param account 账户
     */
    Optional<WebAdminUser> findByAccount(String account);

    boolean existsByAccount(String account);

    /**
     * 根据id和密码查找
     *
     * @param id       账号
     * @param password 密码
     */
    Optional<WebAdminUser> findByIdAndPassword(String id, String password);

    List<WebAdminUser> findByOrganizationNameLike(String organizationName);

    List<WebAdminUser> findByRoleNameLike(String organizationName);


}
