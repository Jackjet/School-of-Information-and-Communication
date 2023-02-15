package d1.project.cms;

import java.io.File;

/**
 * 一些常量，包括枚举等
 *
 * @author Buter
 * @date 2020/3/18 16:09
 */
public class Constants {
    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 1 * 24 * 60 * 60;

    public final static String SUCCESS = "成功";
    public final static String FAIL = "失败:";

    public final static String TEMPLATE_DATA_REPEAT = "该模板名称已存在";
    public final static String DATA_FIELD_DATA_REPEAT = "该字段名称已存在";
    public final static String DATA_IS_NULL = "该数据不存在";

    public final static String TEMPLATE_IN_USED = "抱歉，模板使用中请勿删除";

    public final static String FILE_ROOT = "." + File.separator + "file" + File.separator;
    public final static String RESOURCE_FILE = FILE_ROOT + "resourceFile" + File.separator;
    public final static String COLUMN_FILE = FILE_ROOT + "columnFile" + File.separator;
    public final static String CONTENT_FILE = FILE_ROOT + "contentFile" + File.separator;
    public final static String VIDEO_FILE = FILE_ROOT + "videoFile" + File.separator;

    public final static String PUBLICRESOURCE_FILE = FILE_ROOT + "publicResource" + File.separator + "publicresource.json";

    public final static String WEB_ROOT = System.getProperty("user.dir") + File.separator + "static" + File.separator + "web" + File.separator;
    public final static String WEB_COLUMN = WEB_ROOT + "view" + File.separator;
    public final static String TEMPLATE_ROOT = System.getProperty("user.dir") + File.separator + "static" + File.separator + "templateFile";

    public final static String FILE_CREATE_FAIL = "创建失败";
    public final static String FILE_NOT_EXISTS = "文件不存在";

    public final static String ABSOLUTE_RESOURCE_FILE = System.getProperty("user.dir") + File.separator + "file" + File.separator  + "resourceFile" + File.separator;
}
