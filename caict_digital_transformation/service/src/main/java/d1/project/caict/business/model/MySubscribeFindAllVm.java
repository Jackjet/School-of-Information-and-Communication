package d1.project.caict.business.model;

import d1.project.caict.common.model.TimePageableVm;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class MySubscribeFindAllVm extends TimePageableVm {

    private String type;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
