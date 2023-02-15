package d1.project.caict.business.dao.impl;

import d1.project.caict.business.dao.impl.base.BaseDaoRepository;
import d1.project.caict.business.entity.MyActivities;

import javax.persistence.EntityManager;

/**
 * @author Buter
 * 重写saveall，提高saveall效率
 * @date 2020/9/25 10:07
 */
public class MyActivityDaoImpl extends BaseDaoRepository<MyActivities, String> {
    public MyActivityDaoImpl(EntityManager entityManager) {
        super(MyActivities.class, entityManager);
    }
}
