package d1.project.cms.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.entity.column.Column;
import d1.project.cms.entity.content.Content;
import d1.project.cms.entity.template.Template;
import d1.project.cms.enums.ContentStatus;
import d1.project.cms.enums.YesOrNo;
import d1.project.cms.model.column.ColumnResultVm;
import d1.project.cms.service.column.ColumnService;
import d1.project.cms.service.content.ContentService;
import d1.project.cms.service.template.TemplateService;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * @author
 */
@Service
public class AuditBusiness {
    private final ContentService contentService;
    private final ColumnService columnService;
    private final TemplateService templateService;
    private final TokenService tokenService;

    public AuditBusiness(ContentService contentService, ColumnService columnService, TemplateService templateService, TokenService tokenService) {
        this.contentService = contentService;
        this.columnService = columnService;
        this.templateService = templateService;
        this.tokenService = tokenService;
    }

    /**
     * 查询栏目列表
     *
     * @return 查询结果
     */
    public List<ColumnResultVm> getColumnTree() throws Exception {
        JSONObject params = new JSONObject();
        List<ColumnResultVm> data = this.columnService.findAll(params);
        return this.setTemplateName(data);
    }

    public JSONObject findAllByColumnId(JSONObject params) throws Exception {
        params.put("status", ContentStatus.WAITING_FOR_AUDIT.getName());

        Page<Content> data = this.contentService.findAll(params);
        JSONObject json = (JSONObject) JSON.toJSON(data);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        for (Object obj : json.getJSONArray("content")) {
            Content content = JSON.toJavaObject(((JSONObject) obj),Content.class);

            Column column = this.columnService.findById(((JSONObject) obj).getString("columnId"));
            ((JSONObject) obj).put("columnName", column.getColumnName());

            String updateTime = "";
            if(content.getUpdateTime() != null) {
                updateTime = format.format(content.getUpdateTime().getTime());
            }
            ((JSONObject) obj).put("updateTime", updateTime);
            String createTime = "";
            if(content.getCreateTime() != null) {
                createTime = format.format(content.getCreateTime().getTime());
            }
            ((JSONObject) obj).put("createTime", createTime);
        }
        return json;
    }

    public void pass(HttpServletRequest request,String id) throws DoValidException {
        Content content = this.contentService.findById(id);
        content.setStatus(ContentStatus.ALREADY_PASSED.getName());
        this.tokenService.updateUpdateIdAndTime(request,content);
        this.contentService.save(content);
    }

    public void rejected(HttpServletRequest request,JSONObject params) throws DoValidException {
        Content content = this.contentService.findById(params.getString("id"));
        content.setStatus(ContentStatus.REJECTED.getName());
        content.setReasonForRejection(params.getString("reasonForRejection"));
        this.tokenService.updateUpdateIdAndTime(request,content);
        this.contentService.save(content);
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
}
