package d1.framework.permission.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.*;

import java.beans.PropertyDescriptor;
import java.util.*;

/**
 * @author Buter
 * @date 2020/3/20 15:16
 */
public class MyUtils {

    public static String generate32Id() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    /**
     * 分页
     */
    public static <T> Page<T> pagination(List<T> findAll, JSONObject params) {
        Pageable pageable = getPageable(params.getInteger("page"), params.getInteger("size"), null);
        try {
            // 当前页第一条数据在List中的位置
            int start = (int) pageable.getOffset();
            // 当前页最后一条数据在List中的位置
            int end = (start + pageable.getPageSize()) > findAll.size() ? findAll.size() : (start + pageable.getPageSize());
            // 配置分页数据
            return new PageImpl<>(findAll.subList(start, end), pageable, findAll.size());
        } catch (Exception ex) {
            return new PageImpl<>(findAll.subList(0, 0), pageable, findAll.size());
        }

    }

    public static Pageable getPageable(Integer page, Integer size, Sort sort) {
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }
        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }

    public static Calendar getCalendar(Long aLong) {
        Calendar cal2 = Calendar.getInstance();
        Date date = new Date(aLong);
        cal2.setTime(date);
        return cal2;
    }

    public static <T> T initInsert(T t, String userId) {
        if (t instanceof BaseCreateEntity) {
            ((BaseCreateEntity) t).setId(generate32Id());
            ((BaseCreateEntity) t).setCreateTime(Calendar.getInstance());
            ((BaseCreateEntity) t).setCreateById(userId);
        }
        if (t instanceof BaseCreateAndUpdateEntity) {
            ((BaseCreateAndUpdateEntity) t).setId(generate32Id());
            ((BaseCreateAndUpdateEntity) t).setCreateById(userId);
            ((BaseCreateAndUpdateEntity) t).setCreateTime(Calendar.getInstance());
            ((BaseCreateAndUpdateEntity) t).setUpdateById("");
            ((BaseCreateAndUpdateEntity) t).setUpdateTime(null);
        }
        return t;
    }

    public static <T> T initUpdate(T t, String userId) {
        if (t instanceof BaseCreateAndUpdateEntity) {
            ((BaseCreateAndUpdateEntity) t).setUpdateById(userId);
            ((BaseCreateAndUpdateEntity) t).setUpdateTime(Calendar.getInstance());
        }
        return t;
    }

    public static <S, T> Page<S> listEntity2Model(Page<T> lists, Class<S> cls) {
        String json = JSON.toJSON(lists.getContent()).toString();
        List<S> s = JSON.parseArray(json, cls);
        return new PageImpl<>(s, lists.getPageable(), lists.getTotalElements());
    }

    public static <T> T model2Entity(Object object, Class<T> entityClass) {
        String jsonObject = JSON.toJSON(object).toString();
        return JSON.parseObject(jsonObject, entityClass);
    }

    /**
     * 复制属性
     *
     * @param source 来源
     * @param target 目标
     * @return T
     * @author Kikki  2020/3/28 21:18
     */
    public static <T> T copyProperties(T source, T target) {
        String[] nullProperties = getNullProperties(source);
        BeanUtils.copyProperties(source, target, nullProperties);
        return target;
    }

    /**
     * 获取对象的空属性
     */
    public static String[] getNullProperties(Object src) {
        // 1.获取Bean
        BeanWrapper srcBean = new BeanWrapperImpl(src);
        // 2.获取Bean的属性描述
        PropertyDescriptor[] pds = srcBean.getPropertyDescriptors();
        // 3.获取Bean的空属性
        Set<String> properties = new HashSet<>();
        for (PropertyDescriptor propertyDescriptor : pds) {
            String propertyName = propertyDescriptor.getName();
            Object propertyValue = srcBean.getPropertyValue(propertyName);
//            if (StringUtils.isEmpty(propertyValue)) {
            if (propertyValue == null) {
                srcBean.setPropertyValue(propertyName, null);
                properties.add(propertyName);
            }
        }
        return properties.toArray(new String[0]);
    }

    /**
     * 生成随机数
     */
    public static String makeAuthCode(int places) {
        if (places <= 0) {
            places = 4;
        }

        return RandomStringUtils.random(places, "0123456789");
    }

    public static void throwMsg(boolean b, String msg) throws Exception {
        if (b) {
            throw new Exception(msg);
        }
    }
}
