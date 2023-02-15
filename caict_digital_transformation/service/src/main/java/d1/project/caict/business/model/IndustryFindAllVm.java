package d1.project.caict.business.model;

import d1.project.caict.common.model.TimePageableVm;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class IndustryFindAllVm extends TimePageableVm {

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
