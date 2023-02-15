package d1.project.caict.system.dao;

import d1.project.caict.system.entity.RoleUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:33
 */
public interface RoleUserDao extends JpaRepository<RoleUser, String>, JpaSpecificationExecutor<RoleUser> {

    /**
     * 根据角色id查找
     *
     * @param roleId 角色id
     * @return 列表
     */
    List<RoleUser> findByRoleId(String roleId);

    boolean existsByRoleId(String roleId);

    /**
     * 根据用户id删除
     *
     * @param userId 用户id
     */
    void deleteByUserId(String userId);

    List<RoleUser> findByUserId(String userId);

    void deleteByUserIdIn(Collection<String> userId);

    void deleteByRoleId(String roleId);
}
