package d1.project.caict.system.model.vm;

import d1.framework.webapi.annotation.ApiModelProperty;

import javax.validation.constraints.Size;
import java.util.List;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-16 09:41
 */
public class IdsBaseVm {

    @ApiModelProperty("ids列表")
    @Size(min = 1, message = "无效操作")
    private List<String> ids;

    public List<String> getIds() {
        return ids;
    }

    public void setIds(List<String> ids) {
        this.ids = ids;
    }
}
