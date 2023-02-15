package d1.project.caict.business.dao;

import d1.project.caict.business.entity.MyCollect;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface MyCollectDao extends JpaRepository<MyCollect, String>, JpaSpecificationExecutor<MyCollect> {

    Optional<MyCollect> findFirstByCmsContentIdAndUserId(String id, String userId);

    @Modifying
    @Query(value = "delete from d1_my_collect where cms_content_id =?1 and user_id =?2", nativeQuery = true)
    void deleteByCmsContentIdAndUserId(String cmsContentId, String userId);

}