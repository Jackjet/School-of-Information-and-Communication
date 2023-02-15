package d1.project.caict.system.model;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.hibernate.validator.constraints.Length;

import javax.persistence.Column;

/**
 * d1Project
 *
 * @author kikki
 * @date 2020-09-10 14:43
 */
@ApiModel(value = "MenuTreeInsertVm", description = "菜单基础")
public class MenuTreeBase {
    @ApiModelProperty("路由地址")
    @Length(message = "路由长度超过200字符", min = 0, max = 200)
    private String path;

    @ApiModelProperty("页面地址")
    @Length(message = "页面长度超过200字符", min = 0, max = 200)
    private String component;

    @ApiModelProperty("默认跳转路径")
    @Length(message = "默认跳转路径长度超过200字符", min = 0, max = 200)
    private String redirect;

    @ApiModelProperty("名称")
    @Length(message = "名称长度超过200字符", min = 0, max = 100)
    private String name;

    @ApiModelProperty("标签信息")
    @Length(message = "标签信息长度超过200字符", min = 0, max = 200)
    private String meta;

    @ApiModelProperty("是否有子集：该字段主要是提供前台使用")
    private boolean child;

    @ApiModelProperty("0菜单，1按钮，2新窗口，3内页，4内部功能页面")
    @Column(length = 1)
    private int type;

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public boolean isChild() {
        return child;
    }

    public void setChild(boolean child) {
        this.child = child;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getComponent() {
        return component;
    }

    public void setComponent(String component) {
        this.component = component;
    }

    public String getRedirect() {
        return redirect;
    }

    public void setRedirect(String redirect) {
        this.redirect = redirect;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMeta() {
        return meta;
    }

    public void setMeta(String meta) {
        this.meta = meta;
    }

}
