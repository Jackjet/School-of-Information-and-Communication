package d1.project.caict.business.utils;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenManager;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author: maorui
 * @date: 2020/9/27
 */
public class Utils {
    public static boolean isEmpty(Object str) {
        return str == null || "".equals(str);
    }

    public static String getCurrentUserId(HttpServletRequest request) throws DoValidException {
        JSONObject userToken = TokenManager.getInstance().getUserByToken(request);
        return userToken.getString("id");
    }

    public static Date strToDate(String dateStr) throws ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-ddHH:mm:ss");
        return simpleDateFormat.parse(dateStr);
    }

    public static boolean verfiyHmacSign(String auth, String appId, String timestamp, String appKey) throws Exception {
        return ServiceFactory.getHmacSignService().verifySign(appId, timestamp, appKey, auth.split(" ")[1]);
    }

    public static String getAppKey(String appId) throws IOException {
        File hmacFile = new File("./config/hmac.json");
        if (!hmacFile.exists()) {
            return null;
        }

        JSONArray array = JSONArray.parseArray(IoUtils.readFile(hmacFile));
        for (int i = 0; i < array.size(); i++) {
            JSONObject obj = array.getJSONObject(i);
            String appIdStr = obj.getString("appId");
            if (appId.equals(appIdStr)) {
                return obj.getString("appKey");
            }
        }
        return null;
    }
}
