package d1.project.questionnaire.util;

import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import d1.project.questionnaire.entity.base.BaseCreateEntity;
import org.apache.commons.lang3.RandomStringUtils;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.springframework.beans.BeanUtils;

import java.io.StringReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Kikki 2019/8/28 16:04
 */
public class BaseUtils {

    /**
     * 复制属性创建数据使用
     *
     * @param source 来源
     * @param target 目标类型
     * @param userId 用户id
     * @return T
     * @author Kikki 2019/8/28 16:11
     **/
    public static <T, S> T copyPropertiesInsert(S source, String userId, Class<T> target) throws IllegalAccessException, InstantiationException {
        T t = copyProperties(source, target);
        Date date = new Date();
        if (t instanceof BaseCreateEntity) {
            ((BaseCreateEntity) t).setCreateById(userId);
            ((BaseCreateEntity) t).setCreateTime(date);
        }
        if (t instanceof BaseCreateAndUpdate) {
            ((BaseCreateAndUpdate) t).setCreateById(userId);
            ((BaseCreateAndUpdate) t).setCreateTime(date);
            ((BaseCreateAndUpdate) t).setUpdateById(userId);
            ((BaseCreateAndUpdate) t).setUpdateTime(date);
        }
        return t;
    }

    /**
     * 复制属性更新数据使用
     *
     * @param source 来源
     * @param target 目标类型
     * @param userId 用户id
     * @return T
     * @author Kikki 2019/8/29 21:03
     **/
    public static <T, S> T copyPropertiesUpdate(S source, String userId, Class<T> target) throws IllegalAccessException, InstantiationException {
        T t = copyProperties(source, target);
        if (t instanceof BaseCreateAndUpdate) {
            ((BaseCreateAndUpdate) t).setUpdateById(userId);
            ((BaseCreateAndUpdate) t).setUpdateTime(new Date());
        }
        return t;
    }

    /**
     * 复制属性
     *
     * @param source 来源
     * @param target 目标类型
     * @return T
     * @author Kikki 2019/8/28 16:11
     **/
    public static <T, S> T copyProperties(S source, Class<T> target) throws IllegalAccessException, InstantiationException {
        T t = target.newInstance();
        BeanUtils.copyProperties(source, t);
        return t;
    }

    /**
     * 生成数字验证码
     *
     * @param places 位数
     * @return
     */
    public static String makeAuthCode(int places) {
        if (places <= 0) {
            places = 4;
        }
        return RandomStringUtils.random(places, "0123456789");
    }

    public static <T> List<T> listMapReturnListEntity(List<?> e, Class<T> bClass) {
        String jsonObject = JSONObject.toJSON(e).toString();
        return JSONObject.parseArray(jsonObject, bClass);
    }

    public static <T> T mapReturnEntity(Object e, Class<T> bClass) {
        String jsonObject = JSONObject.toJSON(e).toString();
        return JSONObject.parseObject(jsonObject, bClass);
    }

    public static Map<String, Object> fromXml(String xml) throws Exception {
        Map<String, Object> values = new HashMap<>();
        Document doc = (new SAXReader()).read(new StringReader(xml));
        Element element = doc.getRootElement();
        List list = element.elements();

        for (Object o : list) {
            Element child = (Element) o;
            values.put(child.getName(), child.getStringValue());
        }

        Object returnCode = values.get("return_code");
        if (returnCode != null && returnCode.toString().equals("SUCCESS")) {
            return values;
        } else {
            throw new Exception("返回失败" + xml);
        }
    }

    /**
     * 生成1-16的随机数
     *
     * @return 返回生成的随机数
     */
    public static Integer getNum() {
        Random r = new Random();
        return r.nextInt(16) + 1;
    }

    public static String formatDateByPattern(Date date,String dateFormat){
        SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
        String formatTimeStr = null;
        if (date != null) {
            formatTimeStr = sdf.format(date);
        }
        return formatTimeStr;
    }
//    /***
//     * convert Date to cron ,eg.  "0 07 10 15 1 ? 2016"
//     * @param date  : 时间点
//     * @return
//     */
//    public static String getCron(Date  date){
//        String dateFormat="ss mm HH dd MM ? yyyy";
//        return formatDateByPattern(date, dateFormat);
//    }

    public static Date dateFrom(Date  date,String format) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        String formatString = simpleDateFormat.format(date);
        return simpleDateFormat.parse(formatString);
    }

    /**
     * 获取一个UUID
     *
     * @return UUID
     */
    public static String getUid() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }


    public static List<String> getDayBetweenDates(String begin, String end) throws ParseException {
        SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
        Date dBegin = sd.parse(begin);
        Date dEnd = sd.parse(end);
        List<String> lDate = new ArrayList<String>();
        lDate.add(sd.format(dBegin));
        Calendar calBegin = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calBegin.setTime(dBegin);
        Calendar calEnd = Calendar.getInstance();
        // 使用给定的 Date 设置此 Calendar 的时间
        calEnd.setTime(dEnd);
        // 测试此日期是否在指定日期之后
        while (dEnd.after(calBegin.getTime())) {
            // 根据日历的规则，为给定的日历字段添加或减去指定的时间量
            calBegin.add(Calendar.DAY_OF_MONTH, 1);
            lDate.add(sd.format(calBegin.getTime()));
        }
        return lDate;
    }
}
