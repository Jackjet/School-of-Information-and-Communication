package d1.project.cms.business;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.Constants;
import d1.project.cms.entity.column.Column;
import d1.project.cms.entity.column.ColumnAttachment;
import d1.project.cms.entity.content.Content;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import d1.project.cms.entity.template.Template;
import d1.project.cms.enums.YesOrNo;
import d1.project.cms.model.column.AttachmentVm;
import d1.project.cms.model.column.ColumnResultVm;
import d1.project.cms.service.column.ColumnAttachmentService;
import d1.project.cms.service.column.ColumnService;
import d1.project.cms.service.content.ContentService;
import d1.project.cms.service.repository.RepositoryService;
import d1.project.cms.service.repository.ResourceFileService;
import d1.project.cms.service.template.TemplateService;
import d1.project.cms.utils.MyUtils;
import org.apache.commons.io.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.Vector;

/**
 * @author libin
 */
@Service
public class ColumnBusiness {
    private final ColumnService columnService;
    private final ColumnAttachmentService columnAttachmentService;
    private final RepositoryService repositoryService;
    private final ResourceFileService resourceFileService;
    private final TemplateService templateService;
    private final ContentService contentService;
    private final TokenService tokenService;
    private Logger logger = LoggerFactory.getLogger(ColumnBusiness.class);

    public ColumnBusiness(ColumnService columnService, ColumnAttachmentService columnAttachmentService, RepositoryService repositoryService, ResourceFileService resourceFileService, TemplateService templateService, ContentService contentService, TokenService tokenService) {
        this.columnService = columnService;
        this.columnAttachmentService = columnAttachmentService;
        this.repositoryService = repositoryService;
        this.resourceFileService = resourceFileService;
        this.templateService = templateService;
        this.contentService = contentService;
        this.tokenService = tokenService;
    }

    /**
     * 查询栏目列表
     *
     * @param params 查询条件
     * @return 查询结果
     */
    public List<ColumnResultVm> findAll(JSONObject params) throws DoValidException {
        List<ColumnResultVm> data = this.columnService.findAll(params);

        return this.setTemplateName(data);
    }

    /**
     * 查询栏目列表
     *
     * @param params 查询条件
     * @return 查询结果
     */
    public List<ColumnResultVm> findAllWithAttachment(JSONObject params) throws DoValidException {
        List<ColumnResultVm> data = this.columnService.findAll(params);

        return this.setAttachment(data);
    }

    public JSONObject findById(String id) throws DoValidException {
        Column column = this.columnService.findById(id);
        List<ColumnAttachment> columnAttachment = this.columnAttachmentService.findAllByRelatedId(id);
        JSONObject result = (JSONObject) JSON.toJSON(column);

        result.put("attachment", columnAttachment);
        return result;
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void insert(HttpServletRequest request, JSONObject params) throws DoValidException {
        Column column = JSONObject.toJavaObject(params, Column.class);
        column.setId(MyUtils.generate32Id());
        column.setPower(0);
        if (StringUtils.isEmpty(params.getString("parentId"))) {
            column.setParentId("0");
        }

        if (this.columnService.existsByParentIdAndColumnName(column.getParentId(), column.getColumnName())) {
            throw new DoValidException("该栏目名称已存在");
        }

        this.tokenService.updateCreateIdAndTime(request, column);

        //拷贝模板文件到指定位置
        Template template = this.templateService.findById(column.getTemplateId());
        this.copyFile(template, column.getSeoTitle(), column.getSeoKeyWord());

        //保存栏目附件
        this.saveAttachment(request, column.getId(), params.getJSONArray("attachment"));

        this.columnService.save(column);
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void update(HttpServletRequest request, JSONObject params) throws DoValidException {
        Column data = this.columnService.findById(params.getString("id"));

        if (this.columnService.existsByParentIdAndColumnNameAndIdNot(data.getParentId(), data.getColumnName(), data.getId())) {
            throw new DoValidException("该栏目名称已存在");
        }

        data.setColumnId(params.getString("columnId"));
        data.setColumnName(params.getString("columnName"));
        data.setTemplateId(params.getString("templateId"));
        data.setSeoTitle(params.getString("seoTitle"));
        data.setSeoKeyWord(params.getString("seoKeyWord"));

        //拷贝模板文件到指定位置
        Template template = this.templateService.findById(params.getString("templateId"));
        this.copyFile(template, data.getSeoTitle(), data.getSeoKeyWord());

        //保存栏目附件
        this.saveAttachment(request, params.getString("id"), params.getJSONArray("attachment"));

        this.columnService.save(data);
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void deleteById(String id) throws DoValidException {
        Column data = this.columnService.findById(id);

        if (this.columnService.existsByParentId(id)) {
            throw new DoValidException("抱歉，栏目下包含子栏目，请勿删除");
        }

        List<Content> contents = this.contentService.findAllByColumnId(data.getId());
        //单页的情况，直接删除
        if (contents.size() > 1) {
            throw new DoValidException("抱歉，栏目下包含栏目内容文章，请勿删除");
        } else {
            this.contentService.batchDelete(contents);
        }
        //删除附件文件
        List<ColumnAttachment> columnAttachments = this.columnAttachmentService.findAllByRelatedId(id);
        this.columnAttachmentService.batchDelete(columnAttachments);

        this.columnService.delete(data);
    }

    public void updatePower(JSONObject params) throws DoValidException {
        Column column = JSON.toJavaObject(params, Column.class);
        Column data = this.columnService.findById(column.getId());

        data.setPower(column.getPower());

        this.columnService.save(data);
    }

    public Page<Repository> findAllRepository(JSONObject params) throws DoValidException {
        return this.repositoryService.findAll(params);
    }

    public Page<ResourceFile> fileFindAll(JSONObject params) {
        return this.resourceFileService.findAll(params);
    }

    public List<Template> templateFindAll() {
        return this.templateService.templateFindAll();
    }

    /**
     * 保存附件
     *
     * @param request
     * @param relatedId
     * @param params
     */
    private void saveAttachment(HttpServletRequest request, String relatedId, JSONArray params) throws DoValidException {
        //删除旧附件信息
        List<ColumnAttachment> data = this.columnAttachmentService.findAllByRelatedId(relatedId);
        this.columnAttachmentService.batchDelete(data);

        //添加新附件信息
        List<ColumnAttachment> columnAttachments = new Vector<>();

        for (Object obj : params){
            ColumnAttachment columnAttachment = JSON.toJavaObject((JSONObject)obj, ColumnAttachment.class);
            columnAttachments.add(columnAttachment);
            columnAttachment.setId(MyUtils.generate32Id());
            columnAttachment.setRelatedId(relatedId);
            this.tokenService.updateCreateIdAndTime(request, columnAttachment);
        }

        this.columnAttachmentService.bacthSave(columnAttachments);
    }

    private void copyFile(Template template, String seoTitle, String seoKeyWord) throws DoValidException {
        try {
            if (YesOrNo.YES.getName().equals(template.getIsPcList())) {
                File pcList = new File(Constants.TEMPLATE_ROOT + File.separator + template.getPcListUrl());
                File targetPcList = new File(Constants.WEB_COLUMN + pcList.getName());

                this.checkFileAndCreate(targetPcList);
                FileUtils.copyFile(pcList, targetPcList);

                //替换seo
                this.setSEO(seoTitle, seoKeyWord, targetPcList);
            }

            if (YesOrNo.YES.getName().equals(template.getIsPcDocument())) {
                File pcDocument = new File(Constants.TEMPLATE_ROOT + File.separator + template.getPcDocumentUrl());
                File targetPcDocument = new File(Constants.WEB_COLUMN + pcDocument.getName());

                this.checkFileAndCreate(targetPcDocument);
                FileUtils.copyFile(pcDocument, targetPcDocument);

                //替换seo
                this.setSEO(seoTitle, seoKeyWord, targetPcDocument);
            }

            if (YesOrNo.YES.getName().equals(template.getIsAppList())) {
                File appList = new File(Constants.TEMPLATE_ROOT + File.separator + template.getAppListUrl());
                File targetAppList = new File(Constants.WEB_COLUMN + appList.getName());

                this.checkFileAndCreate(targetAppList);
                FileUtils.copyFile(appList, targetAppList);

                //替换seo
                this.setSEO(seoTitle, seoKeyWord, targetAppList);
            }

            if (YesOrNo.YES.getName().equals(template.getIsAppDocument())) {
                File appDocument = new File(Constants.TEMPLATE_ROOT + File.separator + template.getAppDocumentUrl());
                File targetAppDocument = new File(Constants.WEB_COLUMN + appDocument.getName());

                this.checkFileAndCreate(targetAppDocument);
                FileUtils.copyFile(appDocument, targetAppDocument);

                //替换seo
                this.setSEO(seoTitle, seoKeyWord, targetAppDocument);
            }
        } catch (Exception e) {
            throw new DoValidException(e.getMessage());
        }
    }

    private void checkFileAndCreate(File file) throws Exception {
        if (!file.exists()) {
            file.createNewFile();
        }
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

    private List<ColumnResultVm> setAttachment(List<ColumnResultVm> data) {
        for (ColumnResultVm columnResultVm : data) {
            List<ColumnAttachment> columnAttachment = this.columnAttachmentService.findAllByRelatedId(columnResultVm.getColumnId());
            if (columnAttachment != null) {
                List<AttachmentVm> attachmentVms = new Vector<>();
                for(ColumnAttachment item : columnAttachment) {
                    AttachmentVm attachmentVm = new AttachmentVm();
                    attachmentVm.setId(item.getId());
                    attachmentVm.setFileName(item.getFileName());
                    attachmentVm.setFileDownloadUri(item.getFileDownloadUri());
                    attachmentVm.setFileType(item.getFileType());
                    attachmentVm.setSize(item.getSize());

                    attachmentVms.add(attachmentVm);
                }
                columnResultVm.setAttachmentVm(attachmentVms);
            }
            if (columnResultVm.getHasChildren()) {
                columnResultVm.setChildren(this.setTemplateName(columnResultVm.getChildren()));
            }
        }
        return this.setTemplateName(data);
    }

    private void setSEO(String seoTitle, String seoKeyWord, File file) throws DoValidException {
        logger.info("setSEO-" + file.getName());
        String seo = "<title>";
        String keyWord = "<meta name=\"keywords\" content=\"" + seoKeyWord + "\">";
        String title = "<meta name=\"Description\" content=\"" + seoTitle + "\">";

        String content = keyWord + "\n\t" + title + "\n\t";

        FileReader fileReader = null;
        FileOutputStream fos = null;
        try {
            fileReader = new FileReader(file);
            String fileContent = "";

            char[] buf = new char[1024];
            int len;
            while ((len = fileReader.read(buf)) != -1) {
                String str = new String(buf, 0, len);
                fileContent += str;
            }

            content = content + seo;

            fileContent = fileContent.replaceFirst(seo, content);
            fos = new FileOutputStream(file);
            byte[] bytes = fileContent.getBytes(StandardCharsets.UTF_8);
            fos.write(bytes);
        } catch (Exception e) {
            logger.error("setSEO-" + file.getName() + "-" + e.getMessage());
            throw new DoValidException(e.getMessage());
        } finally {
            //关闭资源
            try {
                if (fileReader != null) {
                    fileReader.close();
                }
            } catch (Exception e) {
            }
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (Exception e) {
            }
        }
    }
}
