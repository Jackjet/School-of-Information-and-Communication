package d1.project.caict.business.dao;

import d1.project.caict.business.entity.Industries;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface IndustryDao extends JpaRepository<Industries, String>, JpaSpecificationExecutor<Industries> {

    @Query(value = "select id,name from d1_industries", nativeQuery = true)
    List<Map<String, String>> findAllNoPage();

}
