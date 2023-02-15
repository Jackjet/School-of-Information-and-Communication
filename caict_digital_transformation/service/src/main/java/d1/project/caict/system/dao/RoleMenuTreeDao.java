package d1.project.caict.system.dao;

import d1.project.caict.system.entity.RoleMenuTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 19:04
 */
public interface RoleMenuTreeDao extends JpaRepository<RoleMenuTree, String>, JpaSpecificationExecutor<RoleMenuTree> {

    boolean existsByRoleId(String roleId);

    List<RoleMenuTree> findByRoleId(String roleId);

    List<RoleMenuTree> findByRoleIdIn(Collection<String> roleId);

    void deleteAllByRoleId(String roleId);

    void deleteByMenuTreeId(String menuTreeId);

    boolean existsByMenuTreeId(String menuTreeId);

    void deleteByRoleId(String roleId);
}
