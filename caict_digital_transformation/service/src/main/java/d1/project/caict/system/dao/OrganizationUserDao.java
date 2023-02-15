package d1.project.caict.system.dao;

import d1.project.caict.system.entity.OrganizationUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Collection;
import java.util.List;

/**
 * 组织用户
 * d1project
 *
 * @author kikki
 * @date 2020-09-09 11:24
 */
public interface OrganizationUserDao extends JpaRepository<OrganizationUser, String>, JpaSpecificationExecutor<OrganizationUser> {

    boolean existsByOrganizationId(String organizationId);
    boolean existsByOrganizationIdIn(Collection<String> organizationId);

    List<OrganizationUser> findByOrganizationId(String organizationId);

    void deleteByUserId(String userId);

    List<OrganizationUser> findByUserId(String userId);

    void deleteByUserIdIn(Collection<String> userId);
}
