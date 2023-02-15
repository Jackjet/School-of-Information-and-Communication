package d1.project.questionnaire.service.file;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.project.questionnaire.model.base.FileRetVm;
import d1.project.questionnaire.service.user.WebAdminUserService;
import d1.project.questionnaire.util.PolyvUtils;
import d1.project.questionnaire.util.TimeUtils;
import net.dcrun.component.qiniu.QiniuService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.File;
import java.text.DecimalFormat;
import java.util.*;


/**
 * @author xuaa
 */
@Service
public class FileService {

    private final WebAdminUserService userService;

    @Value("${d1.framework.storage.qiniu.AK}")
    private String accessKey;
    @Value("${d1.framework.storage.qiniu.SK}")
    private String secretKey;
    @Value("${d1.framework.storage.qiniu.Bucket}")
    private String bucket;
    @Value("${d1.framework.storage.qiniu.DNS}")
    private String DNS;
    @Value("${d1.project.liangxin.polyvUploadVideo.userId}")
    private String polyvUserId;
    @Value("${d1.project.liangxin.polyvUploadVideo.secretKey}")
    private String polyvSecretKey;
    @Value("${d1.project.liangxin.polyvUploadVideo.playsafe.url}")
    private String polyvPlaySafeUrl;
    @Value("${d1.project.liangxin.polyvUploadVideo.url}")
    private String polyvUrl;
    @Value("${d1.project.liangxin.polyvUploadVideo.writetoken}")
    private String polyvWriteToken;


    public FileService( WebAdminUserService userService) {
        this.userService = userService;
    }

    /**
     * 通过api上传到保利威视上
     *
     * @param multipartFile 文件流
     * @return 返回值
     * @throws Exception 抛出异常
     */
    public JSONArray uploadVideo(MultipartFile multipartFile) throws Exception {
        // 获取文件名
        String fileName = multipartFile.getOriginalFilename();
        // 获取文件后缀
        String prefix = fileName.substring(fileName.lastIndexOf("."));
        // 用uuid作为文件名，防止生成的临时文件重复
        File excelFile = File.createTempFile(UUID.randomUUID().toString().replace("-", "").toLowerCase(), prefix);
        // MultipartFile to File
        multipartFile.transferTo(excelFile);

        //业务逻辑
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("title", multipartFile.getOriginalFilename());
        jsonObject.put("tag", "");
        jsonObject.put("desc", "");
        String aa = excelFile.getAbsolutePath();
        //调用接口
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        MediaType type = MediaType.parseMediaType("multipart/form-data");
        // 设置请求的格式类型
        headers.setContentType(type);
        FileSystemResource fileSystemResource = new FileSystemResource(aa);
        MultiValueMap<String, Object> form = new LinkedMultiValueMap<>();
        form.add("writetoken", polyvWriteToken);
        form.add("JSONRPC", jsonObject.toJSONString());
        form.add("Filedata", fileSystemResource);
        HttpEntity<MultiValueMap<String, Object>> files = new HttpEntity<>(form, headers);
        ResponseEntity<String> responseResponseEntity = restTemplate.postForEntity(polyvUrl, files, String.class);
        String body = responseResponseEntity.getBody();
        JSONObject bodyJsonObject = JSONObject.parseObject(body);
        String error = bodyJsonObject.getString("error");
        String data = bodyJsonObject.getString("data");
        //解析结果
        if (!error.equals("0")) {
            throw new Exception("上传失败:" + error);
        }
        //程序结束时，删除临时文件
        deleteFile(excelFile);

        JSONArray jsonArray = JSONObject.parseArray(data);
        return jsonArray;
    }

    /**
     * 为了播放获取 Playsafe Token
     *
     * @param videoId 视频的vid
     * @param request 浏览器
     * @return 返回值
     * @throws Exception 异常
     */
    public String polyvGetPlaysafeToken(String videoId, HttpServletRequest request) throws Exception {
        long l = System.currentTimeMillis();
        String userId = userService.getCurrentUserId(request);
        String body = getPlaySafeToken(videoId, l, userId);
        JSONObject bodyJsonObject = JSONObject.parseObject(body);
        String data = bodyJsonObject.getString("data");
        JSONObject datasonObject = JSONObject.parseObject(data);
        String token = datasonObject.getString("token");
        return token;

    }

    /**
     * 获取签名
     *
     * @param videoId  vid
     * @param l        13位时间戳
     * @param viewerId 观众id
     * @return 返回值
     * @throws Exception 异常
     */
    public String getSign(String videoId, long l, String viewerId) throws Exception {
        Map<String, String> jsonObject = new HashMap<String, String>();
        //用户ID
        jsonObject.put("userId", polyvUserId);
        //视频ID
        jsonObject.put("videoId", videoId);
        //当前13位毫秒级时间戳，10分钟内有效

        jsonObject.put("ts", String.valueOf(l));
        //viewerId
        jsonObject.put("viewerId", viewerId);

        //string	签名，为32位大写的MD5值
        String sign = PolyvUtils.getSign(jsonObject, polyvSecretKey);
        return sign;
    }

    /**
     * 调用第三方url
     *
     * @param videoId  视频vid
     * @param l        时间戳
     * @param viewerId 观众id
     * @return 返回值
     * @throws Exception 异常
     */
    public String getPlaySafeToken(String videoId, long l, String viewerId) throws Exception {
        RestTemplate restTemplate = new RestTemplate();
        //headers
        HttpHeaders requestHeaders = new HttpHeaders();
        requestHeaders.add("user-agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.99 Safari/537.36");
        //body
        MultiValueMap<String, String> requestBody = new LinkedMultiValueMap<>();
        //用户ID
        requestBody.add("userId", polyvUserId);
        //视频ID
        requestBody.add("videoId", videoId);
        //当前13位毫秒级时间戳，10分钟内有效
        requestBody.add("ts", String.valueOf(l));
        //viewerId
        requestBody.add("viewerId", viewerId);
        requestBody.add("sign", getSign(videoId, l, viewerId));
        //HttpEntity
        HttpEntity<MultiValueMap> requestEntity = new HttpEntity<MultiValueMap>(requestBody, requestHeaders);
        //post
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(polyvPlaySafeUrl, requestEntity, String.class);

        HttpStatus statusCode = responseEntity.getStatusCode();
        boolean xxSuccessful = statusCode.is2xxSuccessful();
        if (xxSuccessful) {
            String body = responseEntity.getBody();
            return body;
        } else {
            throw new Exception("失败");
        }
    }

    /**
     * 删除
     *
     * @param files
     */
    private void deleteFile(File... files) {
        for (File file : files) {
            if (file.exists()) {
                file.delete();
            }
        }
    }

    /**
     * 得到后缀名
     *
     * @param file 名称
     * @return 返回值
     */
    private String getFileLastName(MultipartFile file, Integer i) {
        String[] fileArr = file.getOriginalFilename().split("\\.");
        return fileArr[fileArr.length - i];
    }
}
