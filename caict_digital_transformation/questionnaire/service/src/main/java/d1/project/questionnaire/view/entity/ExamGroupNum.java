package d1.project.questionnaire.view.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 题库查看试题列表（试题连表查询）
 *
 * @author baozh
 */
@Data
@Entity
@Immutable
@Subselect("SELECT d1_examination_group.id as id, \n" +
        "d1_examination_group.create_time as create_time, \n" +
        "d1_examination_group.name as name, \n" +
        "table1.num as question_number\n" +
        "FROM d1_examination_group LEFT JOIN \n" +
        "(SELECT d1_question.examination_group_id as group_id,COUNT(d1_question.examination_group_id) as num \n" +
        "FROM d1_question GROUP BY examination_group_id) as table1 \n" +
        "ON d1_examination_group.id = table1.group_id")
public class ExamGroupNum {
    @Id
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "分组名称")
    private String name;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "试题数量")
    private Long questionNumber;

    public Long getQuestionNumber() {
        if (questionNumber == null) {
            questionNumber = 0L;
        }
        return questionNumber;
    }
}
