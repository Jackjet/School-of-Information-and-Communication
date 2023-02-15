package d1.project.caict.system.dao;

import d1.project.caict.system.entity.MenuTree;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

/**
 * 菜单
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:39
 */
public interface MenuTreeDao extends JpaRepository<MenuTree, String>, JpaSpecificationExecutor<MenuTree> {
    boolean existsByParentId(String parentId);

    boolean existsByNameAndParentId(String name, String parentId);

    boolean existsByNameAndParentIdAndIdNot(String name, String parentId, String id);

    List<MenuTree> findByParentId(String parentId);

    List<MenuTree> findByParentIdOrderBySeqAsc(String parentId);

    boolean existsByNameAndIdNot(String name, String id);

    Optional<MenuTree> findByParentIdAndSeq(String parentId, int seq);

    List<MenuTree> findByIdLinkLikeOrderByLevelAsc(String idLink);

    List<MenuTree> findByIdLinkLikeOrderBySeqAsc(String idLink);

    List<MenuTree> findAllByIdInOrderBySeqAsc(Collection<String> id);
    List<MenuTree> findAllByIdInAndTypeInOrderBySeqAsc(Collection<String> id, Collection<Integer> type);

    List<MenuTree> findAllByIdInAndTypeOrderBySeqAsc(Collection<String> id, int type);

    Optional<MenuTree> findFirstByParentIdOrderBySeqDesc(String parentId);

    Optional<MenuTree> findFirstByParentId(String parentId);
}
