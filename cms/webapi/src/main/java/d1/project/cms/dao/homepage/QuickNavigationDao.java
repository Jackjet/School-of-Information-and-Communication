package d1.project.cms.dao.homepage;

import d1.project.cms.entity.homepage.QuickNavigation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author baozh
 */
public interface QuickNavigationDao extends JpaRepository<QuickNavigation,String>, JpaSpecificationExecutor<QuickNavigation> {
    /**
     * 删除该用户下所有导航
     * @param userId 用户信息
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteByUserId(String userId);
}
