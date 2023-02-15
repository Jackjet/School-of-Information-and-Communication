package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * @author Kikki 2019/8/28 15:54
 */
@ApiModel(value = "PageFromVm", description = "分页信息")
public class PageFormVm {

    @ApiModelProperty(value = "第几页,从1开始")
    private Integer page;

    @ApiModelProperty(value = "每页几个，从1开始")
    private Integer size;

    public Integer getPage() {
        return page;
    }

    public void setPage(Integer page) {
        this.page = page;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }
}
