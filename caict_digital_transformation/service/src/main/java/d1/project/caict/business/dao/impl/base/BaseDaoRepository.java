package d1.project.caict.business.dao.impl.base;

import d1.framework.webapi.dao.BatchExecuteRepository;
import org.hibernate.query.NativeQuery;
import org.hibernate.query.Query;
import org.hibernate.query.internal.NativeQueryImpl;
import org.hibernate.transform.Transformers;
import org.hibernate.type.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import javax.persistence.EntityManager;
import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: maorui
 * @date: 2020/10/28
 */
public class BaseDaoRepository<T, ID> extends BatchExecuteRepository<T, ID> {
    private EntityManager entityManager;

    public BaseDaoRepository(Class<T> clazz, EntityManager entityManager) {
        super(clazz, entityManager);
        this.entityManager = entityManager;
    }

    public <T> Page<T> execSqlWithPage(@NotNull String sql, @NotNull Pageable pageable, @NotNull Class<T> clazz) {
        NativeQuery nativeQuery = (NativeQuery) entityManager.createNativeQuery(sql);
        this.addScalar(nativeQuery, clazz);
        Query query = nativeQuery.unwrap(NativeQueryImpl.class).setResultTransformer(Transformers.aliasToBean(clazz));
        query.setFirstResult(pageable.getPageNumber() * pageable.getPageSize());
        query.setMaxResults(pageable.getPageSize());
        return new PageImpl<>(query.getResultList(), pageable, totalCount(sql));
    }

    private Long totalCount(@NotNull String sql) {
        sql = "select count(1) from (" + sql + ") countAlias";
        NativeQuery query = (NativeQuery) entityManager.createNativeQuery(sql);
        BigInteger bigInteger = (BigInteger) query.uniqueResult();
        return bigInteger.longValue();
    }

    /**
     * 添加 字段类型
     *
     * @param nativeQuery sql查询语句
     * @param clazz       类
     */
    private void addScalar(NativeQuery nativeQuery, @NotNull Class clazz) {
        for (Field field : clazz.getDeclaredFields()) {
            Type fieldType = CLASS_TYPE_MAP.get(field.getType());
            if (fieldType == null) {
                continue;
            }
            nativeQuery.addScalar(field.getName(), fieldType);
        }
        if (clazz.getSuperclass() != null) {
            addScalar(nativeQuery, clazz.getSuperclass());
        }
    }

    public static final Map<Class<?>, Type> CLASS_TYPE_MAP = new HashMap<>();

    static {
        CLASS_TYPE_MAP.put(Integer.class, StandardBasicTypes.INTEGER);
        CLASS_TYPE_MAP.put(Long.class, StandardBasicTypes.LONG);
        CLASS_TYPE_MAP.put(Float.class, StandardBasicTypes.FLOAT);
        CLASS_TYPE_MAP.put(Double.class, StandardBasicTypes.DOUBLE);
        CLASS_TYPE_MAP.put(BigDecimal.class, StandardBasicTypes.BIG_DECIMAL);
        CLASS_TYPE_MAP.put(String.class, StandardBasicTypes.STRING);
        CLASS_TYPE_MAP.put(LocalDate.class, LocalDateType.INSTANCE);
        CLASS_TYPE_MAP.put(LocalDateTime.class, LocalDateTimeType.INSTANCE);
        CLASS_TYPE_MAP.put(LocalTime.class, LocalTimeType.INSTANCE);
    }
}
