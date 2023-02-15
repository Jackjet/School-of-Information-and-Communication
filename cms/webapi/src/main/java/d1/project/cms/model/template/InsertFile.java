package d1.project.cms.model.template;

import java.util.List;

/**
 * @author baozh
 */
public class InsertFile {
    private String url;
    private String name;
    private List<Object> content;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Object> getContent() {
        return content;
    }

    public void setContent(List<Object> content) {
        this.content = content;
    }
}
