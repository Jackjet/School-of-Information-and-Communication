package d1.project.cms.dao;

import d1.project.cms.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Buter
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String> , JpaSpecificationExecutor<WebAdminUser> {

    WebAdminUser findByName(String name);
}
