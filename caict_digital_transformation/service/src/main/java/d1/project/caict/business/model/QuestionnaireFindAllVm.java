package d1.project.caict.business.model;

import d1.project.caict.common.model.TimePageableVm;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public class QuestionnaireFindAllVm extends TimePageableVm {

    private String userId;

    private String type;

    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
