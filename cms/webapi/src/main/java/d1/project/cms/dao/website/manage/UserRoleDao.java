package d1.project.cms.dao.website.manage;

import d1.project.cms.entity.website.manage.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserRoleDao extends JpaRepository<UserRole, String> {

    List<UserRole> findAllByUserId(String userId);

    @Query(value = "select r.name from d1_role r left join d1_user_role ur on r.id = ur.role_id where ur.user_id=?1", nativeQuery = true)
    List<String> findAllRoleNameByUserId(String userId);

    List<UserRole> findAllByRoleId(String roleId);

    @Query(value = "select menu.route from d1_menu_tree as menu left join d1_role_permission  on d1_role_permission.menu_tree_id = menu.id where menu.route like '%/%' and d1_role_permission.role_id = ?1 limit 1", nativeQuery = true)
    String findFirstIndex(String roleId);

    UserRole findFirstByUserId(String userId);
}
