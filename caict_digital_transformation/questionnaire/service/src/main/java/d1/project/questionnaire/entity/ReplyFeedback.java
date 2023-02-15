package d1.project.questionnaire.entity;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.util.Date;

/**
 * @author MissouL
 * @desc 管理员回复用户反馈信息
 * @create 2019-12-16 9:43
 **/
@Data
@ApiModel(value = "ReplyFeedback", description = "管理员回复用户反馈信息")
@Entity
@Table(name = "d1_reply_feedback")
public class ReplyFeedback {

    @Id
    @ApiModelProperty(value = "id")
    @Column(length = 50)
    private String id;

    @ApiModelProperty(value = "创建人Id")
    @Column(length = 50)
    private String createById;

    @ApiModelProperty(value = "创建时间", example = "2018-01-01 01:01:01")
    @CreatedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date createTime;

    @ApiModelProperty(value = "修改人Id")
    @Column(length = 50)
    private String updateById;

    @ApiModelProperty(value = "修改时间", example = "2018-01-01 01:01:01")
    @LastModifiedDate
    @Temporal(TemporalType.TIMESTAMP)
    private Date updateTime;

    @ApiModelProperty(value = "信息回复人")
    @Column(length = 50)
    private String name = "管理员";

    @ApiModelProperty(value = "用户反馈信息")
    @Column(length = 50)
    private String feedbackId;

    @Column(columnDefinition = "longtext")
    @ApiModelProperty(value = "回复信息内容")
    private String reply;

    @ApiModelProperty(value = "外部连接")
    private String externalLinks;
}
