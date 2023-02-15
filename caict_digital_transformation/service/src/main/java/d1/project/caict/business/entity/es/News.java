package d1.project.caict.business.entity.es;

import d1.framework.webapi.annotation.ApiModel;
import d1.framework.webapi.annotation.ApiModelProperty;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;


/**
 * 新闻内容
 *
 * @author: maorui
 * @date: 2020/10/9
 */
@ApiModel(value = "News", description = "新闻")
@Document(indexName = "caict", type = "news", refreshInterval = "10s")
public class News {
    @Id
    private String id;

    @ApiModelProperty("栏目id")
    private String columnId;

    @ApiModelProperty("类别")
    private String type;

    @ApiModelProperty("新闻标题")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", fielddata = true)
    private String title;

    @ApiModelProperty("新闻网址")
    private String url;

    @ApiModelProperty("新闻内容")
    @Field(type = FieldType.Text, analyzer = "ik_max_word", searchAnalyzer = "ik_max_word", fielddata = true)
    private String content;

    @ApiModelProperty("访问量")
    private String visitorVolume;

    @ApiModelProperty("时间")
    private String time;

    @ApiModelProperty("权重")
    private int power;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getVisitorVolume() {
        return visitorVolume;
    }

    public void setVisitorVolume(String visitorVolume) {
        this.visitorVolume = visitorVolume;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getPower() {
        return power;
    }

    public void setPower(int power) {
        this.power = power;
    }

    public String getColumnId() {
        return columnId;
    }

    public void setColumnId(String columnId) {
        this.columnId = columnId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
