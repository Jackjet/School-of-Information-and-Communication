package d1.project.caict.business.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.project.caict.common.model.TimePageableVm;

/**
 * @author maorui
 */
@ApiModel(value = "MessageContentFindAllVm", description = "查询所有发送的消息")
public class MessageContentFindAllVm extends TimePageableVm {
    @ApiModelProperty("标题")
    private String title;

    @ApiModelProperty("消息内容")
    private String text;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }
}

