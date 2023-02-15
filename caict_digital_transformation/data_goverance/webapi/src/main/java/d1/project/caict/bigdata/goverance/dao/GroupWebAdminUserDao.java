package d1.project.caict.bigdata.goverance.dao;

import d1.project.caict.bigdata.goverance.entity.GroupWebAdminUser;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface GroupWebAdminUserDao extends JpaRepository<GroupWebAdminUser, String>, JpaSpecificationExecutor<GroupWebAdminUser> {
    boolean existsByName(String name);
}
