package d1.project.questionnaire.view.entity;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.annotations.Immutable;
import org.hibernate.annotations.Subselect;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

/**
 * 考试列表
 *
 * @author baozh
 */
@Data
@Entity
@Immutable
@Subselect("SELECT  d1_examination.id as id, \n" +
        "d1_examination.name as name, \n" +
        "d1_examination.create_time as create_time,\n" +
        "table1.personNum as answer_number, \n" +
        "table2.campNum as related_content\n" +
        "FROM d1_examination  \n" +
        "LEFT JOIN\n" +
        "(SELECT examination_id,COUNT(examination_id) as personNum FROM d1_examination_user GROUP BY examination_id) as table1\n" +
        "ON d1_examination.id = table1.examination_id\n" +
        "LEFT JOIN\n" +
        "(SELECT content_id as examination_id,COUNT(content_id) as campNum FROM d1_learn_plan WHERE type = '3' GROUP BY content_id) as table2\n" +
        "ON d1_examination.id = table2.examination_id")
public class ExamList {
    @Id
    @ApiModelProperty(value = "ID")
    private String id;
    @ApiModelProperty(value = "考试名称")
    private String name;
    @ApiModelProperty(value = "创建时间")
    private Date createTime;
    @ApiModelProperty(value = "答题人数")
    private Long answerNumber;
    @ApiModelProperty(value = "关联训练营")
    private Long relatedContent;

    public Long getAnswerNumber() {
        if (answerNumber == null) {
            answerNumber = 0L;
        }
        return answerNumber;
    }

    public Long getRelatedContent() {
        if (relatedContent == null) {
            relatedContent = 0L;
        }
        return relatedContent;
    }
}
