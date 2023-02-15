package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Entity;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Kikki 2019/8/26 10:06
 */
@Entity
@Table(name = "d1_versions_no")
@ApiModel(value = "VersionsNo", description = "版本号管理表")
public class VersionsNo extends BaseCreateAndUpdate {

    @ApiModelProperty(value = "版本号")
    private String versionsNo;

    @ApiModelProperty(value = "手机类型:安卓0ios1")
    private String type;

    @ApiModelProperty(value = "升级说明")
    @Lob
    private String explains;

    @ApiModelProperty(value = "强制升级 : 0 不强制,1 强制")
    private String forces;

    public String getExplains() {
        return explains;
    }

    public void setExplains(String explains) {
        this.explains = explains;
    }

    public String getForces() {
        return forces;
    }

    public void setForces(String forces) {
        this.forces = forces;
    }

    public String getVersionsNo() {
        return versionsNo;
    }

    public void setVersionsNo(String versionsNo) {
        this.versionsNo = versionsNo;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return "VersionsNo{" +
                "versionsNo='" + versionsNo + '\'' +
                ", type='" + type + '\'' +
                ", explains='" + explains + '\'' +
                ", forces='" + forces + '\'' +
                '}';
    }
}
