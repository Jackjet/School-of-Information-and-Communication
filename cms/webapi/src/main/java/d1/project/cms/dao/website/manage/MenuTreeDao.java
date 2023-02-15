package d1.project.cms.dao.website.manage;

import d1.project.cms.entity.website.manage.MenuTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;


/**
 * @author libin
 */
public interface MenuTreeDao extends JpaRepository<MenuTree, String> {

    @Query(value = "SELECT * FROM d1_menu_tree ORDER BY order_num+0 ASC", nativeQuery = true)
    List<MenuTree> findAllOrderById();

    @Query(value = "SELECT * FROM d1_menu_tree WHERE id IN ?1 ORDER BY order_num+0 ASC", nativeQuery = true)
    List<MenuTree> findOrderById(Collection<String> ids);

    boolean existsByOrderNumAndParentId(Integer orderNum, String parentId);

    boolean existsByParentId(String parentId);
}
