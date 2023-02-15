package d1.project.cms.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import d1.framework.webapi.configuration.DoValidException;
import org.springframework.core.io.ClassPathResource;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

/**
 * @author Buter
 * @date 2020/3/20 15:16
 */
public class SingKeyUtils {
    public static String getAppKeyById(String appid) {
        //在这里实现通过appid获取appkey的内容
        try {
            ClassPathResource classPathResource = new ClassPathResource("signKey.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String singKeys = new String(bytes);
            JSONArray singKeysJson = JSON.parseArray(singKeys);
            for (int i = 0; i < singKeysJson.size(); i++) {
                if (appid.equals(singKeysJson.getJSONObject(i).get("appid").toString())) {
                    return singKeysJson.getJSONObject(i).get("appkey").toString();
                }
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public static String getUrl(String appid) throws Exception {
        try {
            ClassPathResource classPathResource = new ClassPathResource("signKey.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String singKeys = new String(bytes);
            JSONArray singKeysJson = JSON.parseArray(singKeys);
            for (int i = 0; i < singKeysJson.size(); i++) {
                if (appid.equals(singKeysJson.getJSONObject(i).get("appid").toString())) {
                    return singKeysJson.getJSONObject(i).get("url").toString();
                }
            }
        } catch (Exception e) {
            throw new Exception("保存配置异常");
        }
        return null;
    }

    public static List<String> getUrls() throws Exception {
        List<String> urls = new ArrayList<>();
        try {
            ClassPathResource classPathResource = new ClassPathResource("signKey.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String singKeys = new String(bytes);
            JSONArray singKeysJson = JSON.parseArray(singKeys);
            for (int i = 0; i < singKeysJson.size(); i++) {
                if (!StringUtils.isEmpty(singKeysJson.getJSONObject(i).get("url").toString())) {
                    urls.add(singKeysJson.getJSONObject(i).get("url").toString());
                }
            }
        } catch (Exception e) {
            throw new Exception("保存配置异常");
        }
        return urls;
    }

    public static void updateUrl(String appid, String url) throws Exception {
        try {
            ClassPathResource classPathResource = new ClassPathResource("signKey.json");
            byte[] bytes = FileCopyUtils.copyToByteArray(classPathResource.getInputStream());
            String singKeys = new String(bytes);
            JSONArray singKeysJson = JSON.parseArray(singKeys);
            for (int i = 0; i < singKeysJson.size(); i++) {
                if (appid.equals(singKeysJson.getJSONObject(i).get("appid").toString())) {
                    singKeysJson.getJSONObject(i).put("url", url);
                }
            }
            URL sss=classPathResource.getURL();
            BufferedWriter bw = new BufferedWriter(new FileWriter(classPathResource.getURL().getPath()));
            bw.write(singKeysJson.toJSONString());
            bw.flush();
            bw.close();
        } catch (Exception e) {
            throw new Exception("保存配置异常");
        }
    }
}
