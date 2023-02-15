package d1.project.caict.bigdata.goverance.dao;

import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author Buter
 */
public interface WebAdminUserDao extends JpaRepository<WebAdminUser, String>, JpaSpecificationExecutor<WebAdminUser> {

    WebAdminUser findByName(String name);

    boolean existsByName(String name);

    boolean existsByTel(String name);

    boolean existsByNameAndIdNot(String name, String id);

    boolean existsByTelAndIdNot(String tel, String id);

    boolean existsByGroupWebAdminUseName(String groupWebAdminUseName);
}
