package d1.project.caict.business.dao.impl;

import d1.project.caict.business.dao.impl.base.BaseDaoRepository;
import d1.project.caict.business.entity.Messages;

import javax.persistence.EntityManager;

/**
 * @author Buter
 * 重写saveall，提高saveall效率
 * @date 2020/9/25 10:07
 */
public class MessageDaoImpl extends BaseDaoRepository<Messages, String> {

    public MessageDaoImpl(EntityManager entityManager) {
        super(Messages.class, entityManager);
    }
}
