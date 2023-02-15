package d1.project.caict.business.utils;

import java.io.File;

/**
 * @author: maorui
 * @date: 2020/9/29
 */
public class Constants {

    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 24 * 60 * 60;
    /**
     * 注册验证码过期时间，暂定10分钟
     */
    public final static int VER_CODE_EXPIRE_TIME = 10 * 60;


    ///文件目录结构

    public final static String TEMP_ROOT = "." + File.separator + "temp" + File.separator;
    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    public final static String ACTIVITIES = FILES_ROOT + "activities" + File.separator;
    public final static String USERS = FILES_ROOT + "users" + File.separator;
}
