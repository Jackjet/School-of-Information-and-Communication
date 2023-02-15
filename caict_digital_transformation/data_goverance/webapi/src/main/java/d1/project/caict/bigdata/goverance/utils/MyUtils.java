package d1.project.caict.bigdata.goverance.utils;

import com.alibaba.fastjson.JSON;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import d1.framework.webapi.entity.BaseCreateEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.beans.PropertyDescriptor;
import java.text.SimpleDateFormat;
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
     * 两个日期之间的所有月份
     *
     * @param minDate 开始时间
     * @param maxDate 结束时间
     * @return java.util.List<java.lang.String>
     * @author Kikki  2020/6/26 14:53
     */
    public static Map<String, Object> getMonthBetween(String minDate, String maxDate) throws Exception {
        ArrayList<String> result = new ArrayList<String>();
        Map<String, Object> stringLongMap = new LinkedHashMap<>();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");//格式化为年月

        Calendar min = Calendar.getInstance();
        Calendar max = Calendar.getInstance();

        min.setTime(sdf.parse(minDate));
        min.set(min.get(Calendar.YEAR), min.get(Calendar.MONTH), 1);

        max.setTime(sdf.parse(maxDate));
        max.set(max.get(Calendar.YEAR), max.get(Calendar.MONTH), 2);

        Calendar curr = min;
        while (curr.before(max)) {
            stringLongMap.put(sdf.format(curr.getTime()), 0L);
//            result.add(sdf.format(curr.getTime()));
            curr.add(Calendar.MONTH, 1);
        }
        return stringLongMap;
    }

    public static String getTokenByHttpServletRequest(HttpServletRequest request) {
        String auth = request.getHeader("Authorization");
        if (StringUtils.isEmpty(auth)) {
            return null;
        } else {
            String[] authArray = auth.split(" ");
            return "token".equals(authArray[0]) && authArray.length > 1 ? authArray[1] : null;
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

    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    /**
     * 计算列表aList相对于bList的增加的情况，兼容任何类型元素的列表数据结构
     *
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回增加的元素组成的列表
     */
    public static <E> List<E> getAddaListThanbList(List<E> aList, List<E> bList) {
        List<E> addList = new ArrayList<>();
        for (int i = 0; i < aList.size(); i++) {
            if (!myListContains(bList, aList.get(i))) {
                addList.add(aList.get(i));
            }
        }
        return addList;
    }

    /**
     * 计算列表aList相对于bList的减少的情况，兼容任何类型元素的列表数据结构
     *
     * @param aList 本列表
     * @param bList 对照列表
     * @return 返回减少的元素组成的列表
     */
    public static <E> List<E> getReduceaListThanbList(List<E> aList, List<E> bList) {
        List<E> reduceaList = new ArrayList<>();
        for (int i = 0; i < bList.size(); i++) {
            if (!myListContains(aList, bList.get(i))) {
                reduceaList.add(bList.get(i));
            }
        }
        return reduceaList;
    }

    /**
     * 判断元素element是否是sourceList列表中的一个子元素
     *
     * @param sourceList 源列表
     * @param element    待判断的包含元素
     * @return 包含返回 true，不包含返回 false
     */
    private static <E> boolean myListContains(List<E> sourceList, E element) {
        if (sourceList == null || element == null) {
            return false;
        }
        if (sourceList.isEmpty()) {
            return false;
        }
        for (E tip : sourceList) {
            if (element.equals(tip)) {
                return true;
            }
        }
        return false;
    }
}
