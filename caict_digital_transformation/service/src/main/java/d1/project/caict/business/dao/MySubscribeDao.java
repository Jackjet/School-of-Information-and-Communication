package d1.project.caict.business.dao;

import d1.project.caict.business.entity.MySubscribe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface MySubscribeDao extends JpaRepository<MySubscribe, String>, JpaSpecificationExecutor<MySubscribe> {

    Optional<MySubscribe> findFirstByCmsContentIdAndUserId(String id, String userId);

    @Modifying
    @Query(value = "delete from d1_my_subscribe where cms_content_id =?1 and user_id =?2", nativeQuery = true)
    void deleteByCmsContentIdAndUserId(String cmsContentId, String userId);

}
