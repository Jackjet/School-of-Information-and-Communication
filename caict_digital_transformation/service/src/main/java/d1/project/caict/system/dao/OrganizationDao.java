package d1.project.caict.system.dao;

import d1.project.caict.system.entity.Organization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.List;
import java.util.Optional;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 21:34
 */
public interface OrganizationDao extends JpaRepository<Organization, String>, JpaSpecificationExecutor<Organization> {
    boolean existsByNameAndParentId(String name, String parentId);
    boolean existsByName(String name);
    boolean existsByParentId(String parentId);
    List<Organization> findByIdLinkLike(String idLink);

    boolean existsByNameAndParentIdAndIdNot(String name, String parentId, String id);
    boolean existsByNameAndIdNot(String name, String id);

    List<Organization> findByParentId(String parentId);

    Optional<Organization> findByParentIdAndSeq(String parentId, int seq);

    List<Organization> findByIdLinkLikeOrderByLevelAsc(String idLink);

    List<Organization> findByIdLinkLikeOrderBySeqAsc(String idLink);

    Optional<Organization> findFirstByParentIdOrderBySeqDesc(String parentId);

    Optional<Organization> findByName(String name);
}
