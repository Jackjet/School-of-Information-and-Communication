package d1.project.questionnaire.view.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.*;
import java.util.Date;

/**
 * 题库查看试题列表（试题连表查询）
 *
 * @author baozh
 */
@Data
@Entity
@Immutable
@Subselect("SELECT d1_question.id as id,\n" +
        "d1_question.update_time as update_time,\n" +
        "d1_question.examination_group_id as examination_group_id,\n" +
        "d1_question.topic as topic,\n" +
        "d1_question.type as type\n" +
        "FROM d1_question")
public class QuestionBank {
    @Id
    private String id;
    @ApiModelProperty(value = "类型(0单选1多选)")
    private String type;
    @ApiModelProperty(value = "题目")
    @Lob
    private String topic;
    @ApiModelProperty(value = "考试分组id")
    private String examinationGroupId;
    @ApiModelProperty(value = "最后修改时间", example = "2018-01-01 01:01:01")
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;
}
