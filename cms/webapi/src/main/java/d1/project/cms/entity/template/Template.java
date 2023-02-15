package d1.project.cms.entity.template;

import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * 模板
 * @author baozh
 */
@Entity
@Table(name = "d1_template")
@ApiModel(value = "Template", description = "模板")
public class Template extends BaseCreateAndUpdateEntity {
    @ApiModelProperty("模板名称")
    private String name;

    @ApiModelProperty("是否支持PC端列表")
    private String isPcList;

    @ApiModelProperty("Pc端列表模板文件地址")
    private String pcListUrl;

    @ApiModelProperty("是否支持PC端文档")
    private String isPcDocument;

    @ApiModelProperty("PC端文档模板文件地址")
    private String pcDocumentUrl;

    @ApiModelProperty("是否支持APP端列表")
    private String isAppList;

    @ApiModelProperty("APP端列表模板文件地址")
    private String appListUrl;

    @ApiModelProperty("是否支持APP端文档")
    private String isAppDocument;

    @ApiModelProperty("APP端文档模板文件地址")
    private String appDocumentUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIsPcList() {
        return isPcList;
    }

    public void setIsPcList(String isPcList) {
        this.isPcList = isPcList;
    }

    public String getPcListUrl() {
        return pcListUrl;
    }

    public void setPcListUrl(String pcListUrl) {
        this.pcListUrl = pcListUrl;
    }

    public String getIsPcDocument() {
        return isPcDocument;
    }

    public void setIsPcDocument(String isPcDocument) {
        this.isPcDocument = isPcDocument;
    }

    public String getPcDocumentUrl() {
        return pcDocumentUrl;
    }

    public void setPcDocumentUrl(String pcDocumentUrl) {
        this.pcDocumentUrl = pcDocumentUrl;
    }

    public String getIsAppList() {
        return isAppList;
    }

    public void setIsAppList(String isAppList) {
        this.isAppList = isAppList;
    }

    public String getAppListUrl() {
        return appListUrl;
    }

    public void setAppListUrl(String appListUrl) {
        this.appListUrl = appListUrl;
    }

    public String getIsAppDocument() {
        return isAppDocument;
    }

    public void setIsAppDocument(String isAppDocument) {
        this.isAppDocument = isAppDocument;
    }

    public String getAppDocumentUrl() {
        return appDocumentUrl;
    }

    public void setAppDocumentUrl(String appDocumentUrl) {
        this.appDocumentUrl = appDocumentUrl;
    }
}
