package d1.project.caict.system.entity;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author kikki
 */
@Entity
@Table(name = "d1_menu_tree")
@ApiModel(value = "MenuTree", description = "菜单")
public class MenuTree extends BaseCreateAndUpdateEntity {

    @ApiModelProperty("路由地址")
    @Column(length = 200)
    private String path;

    @ApiModelProperty("页面地址")
    @Column(length = 200)
    private String component;

    @ApiModelProperty("默认跳转路径")
    @Column(length = 200)
    private String redirect;

    @ApiModelProperty("名称")
    @Column(length = 100)
    private String name;

    @ApiModelProperty("标签信息")
    @Column(length = 200)
    private String meta;

    @ApiModelProperty("父级id")
    @Column(length = 32)
    private String parentId;

    @ApiModelProperty("父级名称")
    @Column(length = 100)
    private String parentName;

    @ApiModelProperty("序列")
    @Column(length = 2)
    private int seq;

    @ApiModelProperty("id链接")
    @Column(length = 200)
    private String idLink;

    @ApiModelProperty("级别")
    @Column(length = 1)
    private int level;

    @ApiModelProperty("级别中文信息")
    @Column(length = 100)
    private String levelMsg;

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

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getLevelMsg() {
        return levelMsg;
    }

    public void setLevelMsg(String levelMsg) {
        this.levelMsg = levelMsg;
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

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public int getSeq() {
        return seq;
    }

    public void setSeq(int seq) {
        this.seq = seq;
    }

    public String getIdLink() {
        return idLink;
    }

    public void setIdLink(String idLink) {
        this.idLink = idLink;
    }

    public String getParentName() {
        return parentName;
    }

    public void setParentName(String parentName) {
        this.parentName = parentName;
    }
}
