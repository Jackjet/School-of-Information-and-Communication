package d1.project.questionnaire.util;

import d1.project.questionnaire.model.base.TimeVm;
import org.springframework.util.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * 时间相关工具
 *
 * @author MissouL
 */
public class TimeUtils {

    /**
     * 初始化时间，获取当前时间以及半年以后
     *
     * @param timeVm 时间实体
     * @return 时间实体
     */
    public static TimeVm getHalfYear(TimeVm timeVm) throws Exception {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        String startTime = timeVm.getStartTime();
        String endTime = timeVm.getEndTime();
        Date stTime;
        Date edTime;
        // 起始时间为空，则选取当前时间
        if (StringUtils.isEmpty(startTime)) {
            stTime = new Date();
        } else {
            stTime = format.parse(startTime);
        }
        // 结束时间为空则 选取开始时间半年后，
        // 不为空则判断是否大于起始时间，
        // 如果小于，则默认取结束时间前半年
        if (StringUtils.isEmpty(endTime)) {
            edTime = getLastHalfYearFromTime(stTime);
        } else {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd");
            edTime = ft.parse(endTime);
            int compareTo = stTime.compareTo(edTime);
            if (compareTo > 0) {
                stTime = getBeforeHalfYearFromTime(edTime);
            }
        }
        startTime = format.format(stTime);
        endTime = format.format(edTime);
        timeVm.setStartTime(startTime);
        timeVm.setEndTime(endTime);
        return timeVm;
    }

    /**
     * 获取9天前日期
     *
     * @return 日期
     */
    public static String getEightDayBefore() {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        c.add(Calendar.DATE, -9);
        Date d = c.getTime();
        String day = format.format(d);
        day = day + " 00:00:00";
        System.out.print(day);
        return day;
    }

    /**
     * 初始化时间给年月日时间加上时分秒
     *
     * @param startTime 开始时间
     * @param endTime   结束时间
     * @return 时间
     */
    public static TimeVm formatTime(String startTime, String endTime) {
        TimeVm timeVm = new TimeVm();
        if (StringUtils.isEmpty(startTime)) {
            startTime = "1900-01-01";
        }
        if (StringUtils.isEmpty(endTime)) {
            endTime = "2999-12-31";
        }
        startTime = startTime + " 00:00:00";
        endTime = endTime + " 23:59:59";
        timeVm.setStartTime(startTime);
        timeVm.setEndTime(endTime);
        return timeVm;
    }

    /**
     * 获取传入时间的半年后
     *
     * @param time 时间
     * @return 半年后时间
     */
    private static Date getLastHalfYearFromTime(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MONTH, 6);
        time = c.getTime();
        return time;
    }

    /**
     * 获取传入时间的半年前
     *
     * @param time 时间
     * @return 半年前时间
     */
    private static Date getBeforeHalfYearFromTime(Date time) {
        Calendar c = Calendar.getInstance();
        c.setTime(time);
        c.add(Calendar.MONTH, -6);
        time = c.getTime();
        return time;
    }

    /**
     * 获取当年的年月日
     *
     * @return 返回值
     */
    public static String getToday() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date date = new Date(calendar.getTimeInMillis());
        return dateFormat.format(date);
    }
}
