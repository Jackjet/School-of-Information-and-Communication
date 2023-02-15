package d1.project.caict.business.dao;

import d1.project.caict.business.entity.Activities;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface ActivityDao extends JpaRepository<Activities, String>, JpaSpecificationExecutor<Activities> {
}
