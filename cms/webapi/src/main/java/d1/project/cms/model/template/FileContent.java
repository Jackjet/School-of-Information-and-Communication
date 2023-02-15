package d1.project.cms.model.template;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * @author baozh
 */
public class FileContent {
    @ApiModelProperty(value = "文件名")
    private String fileName;
    @ApiModelProperty(value = "文件父级路径")
    private String url;
    @ApiModelProperty(value = "文件内容")
    private List<String> content;

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public List<String> getContent() {
        return content;
    }

    public void setContent(List<String> content) {
        this.content = content;
    }
}
