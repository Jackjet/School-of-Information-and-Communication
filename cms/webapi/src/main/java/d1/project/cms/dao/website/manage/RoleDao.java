package d1.project.cms.dao.website.manage;

import d1.project.cms.entity.website.manage.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface RoleDao extends JpaRepository<Role, String>, JpaSpecificationExecutor<Role> {
    Role findByName(String name);

    Boolean existsByName(String name);
}
