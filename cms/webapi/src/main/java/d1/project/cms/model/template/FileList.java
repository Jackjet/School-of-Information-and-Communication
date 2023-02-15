package d1.project.cms.model.template;

import io.swagger.annotations.ApiModelProperty;

import java.util.List;

/**
 * 文件夹层级
 *
 * @author baozh
 */
public class FileList {
    @ApiModelProperty(value = "文件夹名")
    private String name;
    @ApiModelProperty(value = "类型（0，文件夹，1文件）")
    private String type;

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
}
