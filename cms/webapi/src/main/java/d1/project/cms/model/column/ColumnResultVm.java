package d1.project.cms.model.column;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

public class ColumnResultVm implements Cloneable {
    private String id;
    private String columnId;
    private String columnName;
    private String templateId;
    private String templateName;
    private String isPcList;
    private String pcListUrl;
    private String isPcDocument;
    private String pcDocumentUrl;
    private String isAppList;
    private String appListUrl;
    private String isAppDocument;
    private String appDocumentUrl;
    private Integer power;
    private String seoTitle;
    private String seoKeyWord;
    private String parentId;
    private Boolean hasChildren;
    private List<AttachmentVm> attachmentVm;
    private List<ColumnResultVm> children;

    @Override
    public ColumnResultVm clone() {
        ColumnResultVm columnResultVm = null;
        try{
            columnResultVm = (ColumnResultVm)super.clone();
        }catch(CloneNotSupportedException e) {
            e.printStackTrace();
        }
        return columnResultVm;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public Integer getPower() {
        return power;
    }

    public void setPower(Integer power) {
        this.power = power;
    }

    public String getSeoTitle() {
        return seoTitle;
    }

    public void setSeoTitle(String seoTitle) {
        this.seoTitle = seoTitle;
    }

    public String getSeoKeyWord() {
        return seoKeyWord;
    }

    public void setSeoKeyWord(String seoKeyWord) {
        this.seoKeyWord = seoKeyWord;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public Boolean getHasChildren() {
        return hasChildren;
    }

    public void setHasChildren(Boolean hasChildren) {
        this.hasChildren = hasChildren;
    }

    public List<ColumnResultVm> getChildren() {
        return children;
    }

    public void setChildren(List<ColumnResultVm> children) {
        this.children = children;
    }

    public String getTemplateName() {
        return templateName;
    }

    public void setTemplateName(String templateName) {
        this.templateName = templateName;
    }

    public String getTemplateId() {
        return templateId;
    }

    public void setTemplateId(String templateId) {
        this.templateId = templateId;
    }

    public String getIsPcList() {
        return isPcList;
    }

    public void setIsPcList(String isPcList) {
        this.isPcList = isPcList;
    }

    public String getIsPcDocument() {
        return isPcDocument;
    }

    public void setIsPcDocument(String isPcDocument) {
        this.isPcDocument = isPcDocument;
    }

    public String getIsAppList() {
        return isAppList;
    }

    public void setIsAppList(String isAppList) {
        this.isAppList = isAppList;
    }

    public String getIsAppDocument() {
        return isAppDocument;
    }

    public void setIsAppDocument(String isAppDocument) {
        this.isAppDocument = isAppDocument;
    }

    public List<AttachmentVm> getAttachmentVm() {
        return attachmentVm;
    }

    public void setAttachmentVm(List<AttachmentVm> attachmentVm) {
        this.attachmentVm = attachmentVm;
    }

    public String getPcListUrl() {
        return pcListUrl;
    }

    public void setPcListUrl(String pcListUrl) {
        this.pcListUrl = pcListUrl;
    }

    public String getPcDocumentUrl() {
        return pcDocumentUrl;
    }

    public void setPcDocumentUrl(String pcDocumentUrl) {
        this.pcDocumentUrl = pcDocumentUrl;
    }

    public String getAppListUrl() {
        return appListUrl;
    }

    public void setAppListUrl(String appListUrl) {
        this.appListUrl = appListUrl;
    }

    public String getAppDocumentUrl() {
        return appDocumentUrl;
    }

    public void setAppDocumentUrl(String appDocumentUrl) {
        this.appDocumentUrl = appDocumentUrl;
    }
}
