package d1.project.questionnaire.entity;

import d1.project.questionnaire.entity.base.BaseCreateAndUpdate;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.*;
import java.util.Date;

/**
 * @author MissouL
 */
@EqualsAndHashCode(callSuper = true)
@ApiModel(value = "Almanac", description = "黄历")
@Entity
@Table(name = "d1_almanac")
@Data
public class Almanac extends BaseCreateAndUpdate {
    @ApiModelProperty(value = "公历")
    @Temporal(TemporalType.DATE)
    private Date solarCalendar;
    @ApiModelProperty(value = "阴历")
    private String lunarCalendar;
    @ApiModelProperty(value = "中国传统节日")
    private String trFestival;
    @ApiModelProperty(value = "世界通用节日")
    private String festival;
    @ApiModelProperty(value = "宜")
    private String yi;
    @ApiModelProperty(value = "忌")
    private String ji;
    @ApiModelProperty(value = "干支纪年-年份")
    private String ganZhiYear;
    @ApiModelProperty(value = "干支纪年-月")
    private String ganZhiMonth;
    @ApiModelProperty(value = "干支纪年-日")
    private String ganZhiData;
    @ApiModelProperty(value = "生肖")
    private String zodiac;
    @ApiModelProperty(value = "阴历月")
    private String lunarMonth;
    @ApiModelProperty(value = "阴历日")
    private String lunarData;
    @ApiModelProperty(value = "节气")
    private String solarTerms;
    @ApiModelProperty(value = "黄历新说")
    @Lob
    private String newIdea;
}
