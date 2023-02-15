package d1.project.caict.system.service;

import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;
import d1.project.caict.common.utils.BaseUtils;
import d1.project.caict.system.dao.OrganizationUserDao;
import d1.project.caict.system.entity.OrganizationUser;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Collection;
import java.util.List;

/**
 * 组织机构用户
 * d1Project
 *
 * @author kikki
 * @date 2020-09-09 11:27
 */
@Service
public class OrganizationUserService {


    private final OrganizationUserDao organizationUserDao;

    public OrganizationUserService(OrganizationUserDao organizationUserDao) {
        this.organizationUserDao = organizationUserDao;
    }

    /**
     * 新增
     *
     * @param model 传输模型
     */
    public void insert(OrganizationUser model, HttpServletRequest request) throws DoValidException {
        model.setId(BaseUtils.generate32Id());
        TokenManager.getInstance().updateCreateIdAndTime(request, model);
        organizationUserDao.save(model);
    }

    void deleteByUserId(String userId) {
        organizationUserDao.deleteByUserId(userId);
    }

    void deleteByUserIdIn(Collection<String> userId) {
        organizationUserDao.deleteByUserIdIn(userId);
    }

    boolean existsByOrganizationId(String organizationId) {
        return organizationUserDao.existsByOrganizationId(organizationId);
    }

    boolean existsByOrganizationIdIn(Collection<String> organizationId){
        return organizationUserDao.existsByOrganizationIdIn(organizationId);
    }

    public List<OrganizationUser> findByOrganizationId(String organizationId) {
        return organizationUserDao.findByOrganizationId(organizationId);
    }

    public List<OrganizationUser> findByUserId(String userId) {
        return organizationUserDao.findByUserId(userId);
    }
}
