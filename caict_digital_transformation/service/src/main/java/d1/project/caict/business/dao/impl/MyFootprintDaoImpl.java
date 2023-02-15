package d1.project.caict.business.dao.impl;

import d1.project.caict.business.dao.impl.base.BaseDaoRepository;
import d1.project.caict.business.entity.MyFootprint;

import javax.persistence.EntityManager;

/**
 * @author Buter
 * 重写saveall，提高saveall效率
 * @date 2020/9/25 10:07
 */
public class MyFootprintDaoImpl extends BaseDaoRepository<MyFootprint, String> {

    public MyFootprintDaoImpl(EntityManager entityManager) {
        super(MyFootprint.class, entityManager);
    }
}
