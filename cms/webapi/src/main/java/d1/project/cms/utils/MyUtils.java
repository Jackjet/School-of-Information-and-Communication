package d1.project.cms.utils;

import d1.framework.webapi.configuration.DoValidException;

import java.util.UUID;

/**
 * @author Buter
 * @date 2020/3/20 15:16
 */
public class MyUtils {

    public static String generate32Id() {
        return UUID.randomUUID().toString().replace("-", "").toLowerCase();
    }

    public static void throwMsg(boolean b, String msg) throws Exception {
        if (b) {
            throw new DoValidException(msg);
        }
    }
}
