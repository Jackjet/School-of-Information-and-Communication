package d1.project.questionnaire.util;

import org.springframework.util.DigestUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 获取签名
 *
 * @author xuaa
 */
public class PolyvUtils {

    /**
     * 过取签名
     *
     * @param maps      参数
     * @param secureKey 秘钥
     * @return 返回值
     * @throws Exception 抛出异常
     */
    public static String getSign(Map<String, String> maps, String secureKey) throws Exception {
        List<String> keys = new ArrayList<>(maps.keySet());
        List<String> tmp = new ArrayList<>();
        Collections.sort(keys);
        for (String key : keys) {
            if (null != maps.get(key) && maps.get(key).length() > 0) {
                tmp.add(key + maps.get(key));
            }
        }
        String join = secureKey + String.join("", tmp) + secureKey;
        String s = DigestUtils.md5DigestAsHex(join.getBytes());
        return s.toUpperCase();
    }
}
