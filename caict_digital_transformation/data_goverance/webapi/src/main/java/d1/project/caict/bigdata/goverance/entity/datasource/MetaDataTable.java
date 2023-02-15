package d1.project.caict.bigdata.goverance.entity.datasource;


import d1.framework.webapi.entity.BaseCreateAndUpdateEntity;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author maoyy
 */

@Entity
@Table(name = "d1_meta_data_table")
@ApiModel(value = "MetaDataTable", description = "数据源元数据表")
public class MetaDataTable extends BaseCreateAndUpdateEntity {
    @Column(length = 50)
    @ApiModelProperty("数据源id")
    private String dataSourceId;

    @Column(length = 50)
    @ApiModelProperty("数据源名称")
    private String dataSourceName;

    @Column(length = 50)
    @ApiModelProperty("表名")
    private String tableName;

    @Column(length = 256)
    @ApiModelProperty("表描述")
    private String tableDesc;

    @Column(columnDefinition = "TEXT")
    @ApiModelProperty("表字段")
    private String tableFields;

    @Column(length = 10)
    @ApiModelProperty("状态")
    private String status;

    @ApiModelProperty("数据条数")
    private String count;

    @Column(length = 512)
    @ApiModelProperty("备注")
    private String remark;

    public String getDataSourceId() {
        return dataSourceId;
    }

    public void setDataSourceId(String dataSourceId) {
        this.dataSourceId = dataSourceId;
    }

    public String getDataSourceName() {
        return dataSourceName;
    }

    public void setDataSourceName(String dataSourceName) {
        this.dataSourceName = dataSourceName;
    }

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public String getTableDesc() {
        return tableDesc;
    }

    public void setTableDesc(String tableDesc) {
        this.tableDesc = tableDesc;
    }

    public String getTableFields() {
        return tableFields;
    }

    public void setTableFields(String tableFields) {
        this.tableFields = tableFields;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}

