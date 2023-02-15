package d1.project.questionnaire.model.base;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuaa
 */
@Data
@ApiModel(value = "FileRetVm", description = "文件返回Vm")
public class FileRetVm {
    @ApiModelProperty(value = "文件名")
    private String name;
    @ApiModelProperty(value = "文件相对路径")
    private String path;
    @ApiModelProperty(value = "视频 音频的封面图")
    private String pic;
    @ApiModelProperty(value = "文件大小")
    private double size;
    @ApiModelProperty(value = "文件类型")
    private String type;
}
