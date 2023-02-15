package d1.project.caict.business.model;

import d1.project.caict.common.model.TimePageableVm;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class UserFindAllVm extends TimePageableVm {

    private String name;

    private String linkmanName;

    private String source;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLinkmanName() {
        return linkmanName;
    }

    public void setLinkmanName(String linkmanName) {
        this.linkmanName = linkmanName;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }
}
