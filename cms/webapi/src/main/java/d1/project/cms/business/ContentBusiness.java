package d1.project.cms.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.entity.column.Column;
import d1.project.cms.entity.content.Content;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import d1.project.cms.entity.template.DataField;
import d1.project.cms.entity.template.Template;
import d1.project.cms.enums.ContentStatus;
import d1.project.cms.enums.DataFieldType;
import d1.project.cms.enums.ResourceFileType;
import d1.project.cms.enums.YesOrNo;
import d1.project.cms.model.column.ColumnResultVm;
import d1.project.cms.service.column.ColumnService;
import d1.project.cms.service.content.ContentService;
import d1.project.cms.service.repository.RepositoryService;
import d1.project.cms.service.repository.ResourceFileService;
import d1.project.cms.service.template.DataFieldService;
import d1.project.cms.service.template.TemplateService;
import d1.project.cms.utils.MyUtils;
import d1.project.cms.utils.SingKeyUtils;
import net.dcrun.component.security.HmacSignService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Vector;

/**
 * @author
 */
@Service
public class ContentBusiness {
    private final ContentService contentService;
    private final ColumnService columnService;
    private final DataFieldService dataFieldService;
    private final TemplateService templateService;
    private final RepositoryService repositoryService;
    private final ResourceFileService resourceFileService;
    private final TokenService tokenService;
    private final HmacSignService hmacSignService;
    @Value("${hmac.appid}")
    private String appid;
    @Value("${hmac.appkey}")
    private String appkey;

    public ContentBusiness(ContentService contentService, ColumnService columnService, DataFieldService dataFieldService, TemplateService templateService, RepositoryService repositoryService, ResourceFileService resourceFileService, TokenService tokenService, HmacSignService hmacSignService) {
        this.contentService = contentService;
        this.columnService = columnService;
        this.dataFieldService = dataFieldService;
        this.templateService = templateService;
        this.repositoryService = repositoryService;
        this.resourceFileService = resourceFileService;
        this.tokenService = tokenService;
        this.hmacSignService = hmacSignService;
    }

    /**
     * 查询栏目列表
     *
     * @return 查询结果
     */
    public List<ColumnResultVm> getColumnTree() throws DoValidException {
        JSONObject params = new JSONObject();
        List<ColumnResultVm> data = this.columnService.findAll(params);
        return this.setTemplateName(data);
    }

    public JSONObject findAllByColumnId(JSONObject params) throws DoValidException {
        try {
            Page<Content> data = this.contentService.findAll(params);
            JSONObject json = (JSONObject) JSON.toJSON(data);

            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            for (Object obj : json.getJSONArray("content")) {
                Content content = JSON.toJavaObject(((JSONObject) obj), Content.class);

                Column column = this.columnService.findById(((JSONObject) obj).getString("columnId"));
                ((JSONObject) obj).put("columnName", column.getColumnName());

                String updateTime = "";
                if (content.getUpdateTime() != null) {
                    updateTime = format.format(content.getUpdateTime().getTime());
                }
                ((JSONObject) obj).put("updateTime", updateTime);
                String createTime = "";
                if (content.getCreateTime() != null) {
                    createTime = format.format(content.getCreateTime().getTime());
                }
                ((JSONObject) obj).put("createTime", createTime);
            }
            return json;
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }


    public List<DataField> fieldFindAll(JSONObject params) {
        return this.dataFieldService.findAllByTemplateIdAndType(params.getString("templateId"), DataFieldType.DOCUMENT.getName());
    }

    public void updatePower(HttpServletRequest request, JSONObject params) throws DoValidException {
        Content content = this.contentService.findById(params.getString("id"));
        content.setPower(params.getInteger("power"));

        this.tokenService.updateUpdateIdAndTime(request, content);
        this.contentService.save(content);
    }

    public void insert(HttpServletRequest request, JSONObject params) throws DoValidException {
        Content content = JSON.toJavaObject(params, Content.class);
        Column column = this.columnService.findById(content.getColumnId());

        Template template = null;
        try {
            template = this.templateService.findById(column.getTemplateId());
        } catch (DoValidException e) {
            throw new DoValidException("模板异常，请重新核对使用模板！");
        }

        if (YesOrNo.YES.getName().equals(template.getIsAppList()) || YesOrNo.YES.getName().equals(template.getIsPcList())) {
            content.setStatus(ContentStatus.WAITING_FOR_AUDIT.getName());
        } else {
            content.setStatus(ContentStatus.ALREADY_PASSED.getName());
        }
        content.setId(MyUtils.generate32Id());

        content.setVisitorVolume(0L);
        content.setPower(params.getInteger("power"));

        content.setListContent(this.copyContent(content.getColumnId(), content.getContent()));

        this.tokenService.updateCreateIdAndTime(request, content);
        this.tokenService.updateUpdateIdAndTime(request, content);
        this.contentService.save(content);
        //接口推送数据
        pushContent();
    }

    public void update(HttpServletRequest request, JSONObject params) throws DoValidException {
        Content content = JSON.toJavaObject(params, Content.class);

        Content data = this.contentService.findById(params.getString("id"));
        Column column = this.columnService.findById(data.getColumnId());

        Template template = null;
        try {
            template = this.templateService.findById(column.getTemplateId());
        } catch (DoValidException e) {
            throw new DoValidException("模板异常，请重新核对使用模板！");
        }

        if (YesOrNo.YES.getName().equals(template.getIsAppList()) || YesOrNo.YES.getName().equals(template.getIsPcList())) {
            data.setStatus(ContentStatus.WAITING_FOR_AUDIT.getName());
        } else {
            data.setStatus(ContentStatus.ALREADY_PASSED.getName());
        }

        data.setPower(content.getPower());
        data.setTitle(content.getTitle());
        data.setContent(content.getContent());

        data.setListContent(this.copyContent(data.getColumnId(), data.getContent()));

        this.tokenService.updateUpdateIdAndTime(request, data);
        this.contentService.save(data);
    }

    public JSONObject getContentById(String id) throws DoValidException {
        try {
            JSONObject result = new JSONObject();
            Content content = this.contentService.findById(id);
            if (content != null) {
                String seoTitle = "";
                String seoKeyWord = "";
                Column column = this.columnService.findFirstByColumnId(content.getColumnId());
                if (column != null) {
                    seoTitle = column.getSeoTitle();
                    seoKeyWord = column.getSeoKeyWord();
                }

                result = (JSONObject) JSON.toJSON(content);
                result.put("seoTitle", seoTitle);
                result.put("seoKeyWord", seoKeyWord);
            }
            return result;
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }

    public Content findById(String id) throws DoValidException {
        try {
            return this.contentService.findById(id);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }

    public void callback(HttpServletRequest request, String url) throws DoValidException {
        String appid = request.getHeader("appid");
        try {
            SingKeyUtils.updateUrl(appid, url);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }

    public void copy(String id, String columnId) throws DoValidException {
        Content data = this.contentService.findById(id);

        if (data.getColumnId().equals(columnId)) {
            throw new DoValidException("抱歉，同栏目下不可复制");
        }

        Content content = data.clone();
        content.setId(MyUtils.generate32Id());
        content.setColumnId(columnId);

        this.contentService.save(content);
    }

    public List<Column> findAllColumn() {
        List<String> templateIds = this.templateService.findAllByIsPcListOrIsAppList("1", "1");
        return this.columnService.findAllByTemplateIdIn(templateIds);
    }

    public void deleteById(String id) throws DoValidException {
        Content content = this.contentService.findById(id);
        this.contentService.delete(content);
    }

    public JSONObject getContentByColumnId(JSONObject params) throws DoValidException {
        params.put("status", ContentStatus.ALREADY_PASSED.getName());
        if ("-1".equals(params.getString("size"))) {
            params.put("size", 10000);
        }

        JSONObject result = new JSONObject();
        try {
            Page<Content> contents = this.contentService.findAll(params);
            result = (JSONObject) JSON.toJSON(contents);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }

        JSONArray content = result.getJSONArray("content");
        if (content.size() > 0) {
            String seoTitle = "";
            String seoKeyWord = "";
            Column column = this.columnService.findFirstByColumnId(params.getString("columnId"));
            if (column != null) {
                seoTitle = column.getSeoTitle();
                seoKeyWord = column.getSeoKeyWord();
            }

            for (Object obj : content) {
                ((JSONObject) obj).put("seoTitle", seoTitle);
                ((JSONObject) obj).put("seoKeyWord", seoKeyWord);
            }
        }
        return result;
    }

    public JSONObject getAllContentByColumnId(JSONObject params) throws DoValidException {
        if ("-1".equals(params.getString("size"))) {
            params.put("size", 10000);
        }

        JSONObject result = new JSONObject();
        try {
            String columnId = params.getString("columnId");
            if(StringUtils.isEmpty(columnId)) {
                throw new DoValidException("栏目id不能为空");
            }

            List<Content> data = this.contentService.findAllByColumnIdAndStatusOrderByPowerAsc(columnId,ContentStatus.ALREADY_PASSED.getName());

            List<String> columnIds = this.columnService.findAllColumnIdsByParentId(columnId);
            if(columnIds != null && columnIds.size() > 0){
                List<Content> childColumnContents = this.contentService.findAllByColumnIdInAndStatusOrderByPowerAsc(columnIds,ContentStatus.ALREADY_PASSED.getName());
                data.addAll(childColumnContents);
            }

            SpecificationBuilder<Content> builder = new SpecificationBuilder<Content>().init(params);
            Page<Content> contents = builder.listToPage(data,builder.getPageable());
            result = (JSONObject) JSON.toJSON(contents);
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }

        JSONArray content = result.getJSONArray("content");
        if (content.size() > 0) {
            String seoTitle = "";
            String seoKeyWord = "";
            Column column = this.columnService.findFirstByColumnId(params.getString("columnId"));
            if (column != null) {
                seoTitle = column.getSeoTitle();
                seoKeyWord = column.getSeoKeyWord();
            }

            for (Object obj : content) {
                ((JSONObject) obj).put("seoTitle", seoTitle);
                ((JSONObject) obj).put("seoKeyWord", seoKeyWord);
            }
        }
        return result;
    }

    public JSONObject getContentByColumnId2(String columnId) {
        JSONObject result = new JSONObject();
        Content content = this.contentService.findFirstByColumnId(columnId);
        if (content != null) {
            String seoTitle = "";
            String seoKeyWord = "";
            Column column = this.columnService.findFirstByColumnId(columnId);
            if (column != null) {
                seoTitle = column.getSeoTitle();
                seoKeyWord = column.getSeoKeyWord();
            }

            result = (JSONObject) JSON.toJSON(content);
            result.put("seoTitle", seoTitle);
            result.put("seoKeyWord", seoKeyWord);
        }
        return result;
    }

    public Page<Repository> findAllRepository(JSONObject params) throws DoValidException {
        return this.repositoryService.findAll(params);
    }

    public Page<ResourceFile> fileFindAll(JSONObject params) throws DoValidException {
        List<String> types = new Vector<>();
        types.add(ResourceFileType.PICTURE.getName());
        types.add(ResourceFileType.FOLDER.getName());

        params.put("type", types);
        return this.resourceFileService.findAllByType(params);
    }

    private List<ColumnResultVm> setTemplateName(List<ColumnResultVm> data) {
        for (ColumnResultVm columnResultVm : data) {
            Template template = this.templateService.getTemplate(columnResultVm.getTemplateId());
            if (template != null) {
                columnResultVm.setTemplateName(template.getName());
                columnResultVm.setIsPcList(template.getIsPcList());
                columnResultVm.setIsPcDocument(template.getIsPcDocument());
                columnResultVm.setIsAppList(template.getIsAppList());
                columnResultVm.setIsAppDocument(template.getIsAppDocument());

                if (template.getIsPcList() == null) {
                    columnResultVm.setIsPcList(YesOrNo.NO.getName());
                }
                if (template.getIsPcDocument() == null) {
                    columnResultVm.setIsPcDocument(YesOrNo.NO.getName());
                }
                if (template.getIsAppList() == null) {
                    columnResultVm.setIsAppList(YesOrNo.NO.getName());
                }
                if (template.getIsAppDocument() == null) {
                    columnResultVm.setIsAppDocument(YesOrNo.NO.getName());
                }

                if (!StringUtils.isEmpty(template.getPcListUrl())) {
                    columnResultVm.setPcListUrl(template.getPcListUrl().replace("/pc/", ""));
                }
                if (!StringUtils.isEmpty(template.getPcDocumentUrl())) {
                    columnResultVm.setPcDocumentUrl(template.getPcDocumentUrl().replace("/pc/", ""));
                }
                if (!StringUtils.isEmpty(template.getAppListUrl())) {
                    columnResultVm.setAppListUrl(template.getAppListUrl().replace("/pc/", ""));
                }
                if (!StringUtils.isEmpty(template.getAppDocumentUrl())) {
                    columnResultVm.setAppDocumentUrl(template.getAppDocumentUrl().replace("/pc/", ""));
                }
            }
            if (columnResultVm.getHasChildren()) {
                columnResultVm.setChildren(this.setTemplateName(columnResultVm.getChildren()));
            }
        }
        return data;
    }

    private String copyContent(String columnId, String content) throws DoValidException {
        Column column = this.columnService.findById(columnId);
        List<DataField> listDataFields = this.dataFieldService.findAllByTemplateIdAndType(column.getTemplateId(), DataFieldType.LIST.getName());
        List<DataField> docDataFields = this.dataFieldService.findAllByTemplateIdAndType(column.getTemplateId(), DataFieldType.DOCUMENT.getName());
        JSONObject contentJson = JSON.parseObject(content);

        JSONObject json = new JSONObject();
        JSONObject values = contentJson.getJSONObject("values");
        JSONObject types = contentJson.getJSONObject("types");

        JSONObject listValues = new JSONObject();
        JSONObject listTypes = new JSONObject();

        for (DataField listDataField : listDataFields) {
            for (DataField docDataField : docDataFields) {
                if (listDataField.getField().equals(docDataField.getField())) {
                    String key = listDataField.getField();
                    Object value = values.get(key);
                    Object type = types.get(key);
                    listValues.put(key, value);
                    listTypes.put(key, type);
                    break;
                }
            }
        }
        json.put("values", listValues);
        json.put("types", listTypes);

        return json.toJSONString();
    }

    private void pushContent() {
        try {
            List<String> urls = SingKeyUtils.getUrls();
            List<Content> contentList = contentService.findBySync();

            for (String url : urls) {
                //数据分割，每50条一次推送
                int toIndex = 50;
                int listSize=contentList.size();
                for (int i = 0; i < listSize; i += 50) {
                    if (i + 50 > listSize) {
                        toIndex = listSize - i;
                    }
                    List<Content> newList = contentList.subList(i, i + toIndex);
                    pushContentCallback(url, newList);
                }
            }
        } catch (Exception e) {

        }
    }

    private void pushContentCallback(String url, List<Content> contentList) {
        try {
            Calendar calendar = Calendar.getInstance();
            String data = JSON.toJSONString(contentList);
            String timestamp = String.valueOf(calendar.getTimeInMillis());
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.add("Content-Type", "application/json");
            headers.add("timestamp", timestamp);
            headers.add("appid", "cms");
            headers.add("Authorization", "sign " + hmacSignService.sign(appid, timestamp, appkey));
            HttpEntity<String> httpEntity = new HttpEntity<>(data, headers);
            ResponseEntity<JSONObject> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, JSONObject.class);
            if (response.getStatusCodeValue() == 200) {
                String code = response.getBody().getString("code");
                if ("1".equals(code)) {
                    for (Content content : contentList) {
                        content.setSync(1);
                        contentService.save(content);
                    }
                }
            }
        } catch (Exception e) {

        }
    }
}
