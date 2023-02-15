package d1.project.caict.business.model;

import d1.project.caict.common.model.TimePageableVm;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class ActivityFindAllVm extends TimePageableVm {

    private String title;

    private String status;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
