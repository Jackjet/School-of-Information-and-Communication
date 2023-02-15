package d1.project.questionnaire.entity.base;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;


/**
 * @author Kikki 2019/8/26 10:10
 */
@EqualsAndHashCode(callSuper = true)
@MappedSuperclass
@Data
public class BaseGua64 extends BaseCreateAndUpdate {

    private String xh;
    private String word;
    private String word1;
    private String word2;
    @Column(name = "[desc]", columnDefinition = "longtext")
    private String desc;
    @Column(columnDefinition = "longtext")
    private String gdesc;
    private String video_01;
    @Column(columnDefinition = "longtext")
    private String vdesc_01;
    private String video_02;
    @Column(columnDefinition = "longtext")
    private String vdesc_02;
    private String zgua;
    private String cgua;
    private String hgua;
    private String Jgua;
    @Column(columnDefinition = "longtext")
    private String yao;
}
