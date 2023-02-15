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
@Table(name = "d1_job_datasource")
@ApiModel(value = "JobDataSource", description = "数据源表")
public class JobDataSource extends BaseCreateAndUpdateEntity {
    @Column(length = 50)
    @ApiModelProperty("名称")
    private String name;
    @Column(length = 20)
    @ApiModelProperty("数据源分类")
    private String catalog;
    @Column(length = 20)
    @ApiModelProperty("数据源类型")
    private String type;
    @Column(length = 30)
    @ApiModelProperty("数据源地址")
    private String ipAddress;
    @Column(length = 10)
    @ApiModelProperty("状态")
    private String status;
    @Column(length = 30)
    @ApiModelProperty("用户")
    private String userName;
    @Column(length = 30)
    @ApiModelProperty("密码")
    private String password;
    @Column(length = 255)
    @ApiModelProperty("jdbc url")
    private String jdbcUrl;
    @Column(length = 100)
    @ApiModelProperty("jdbc驱动")
    private String jdbcServer;
    @Column(length = 512)
    @ApiModelProperty("备注")
    private String remark;
    @Column(length = 100)
    @ApiModelProperty("zk地址")
    private String zkAddress;
    @Column(length = 100)
    @ApiModelProperty("地址")
    private String address;
    @Column(length = 80)
    @ApiModelProperty("数据库名称")
    private String dbName;
    @Column(length = 100)
    @ApiModelProperty("目录")
    private String folder;
    @Column(length = 200)
    @ApiModelProperty("header")
    private String header;
    @Column(length = 256)
    @ApiModelProperty("URL参数")
    private String urlParams;
    @Column(length = 512)
    @ApiModelProperty("body")
    private String body;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCatalog() {
        return catalog;
    }

    public void setCatalog(String catalog) {
        this.catalog = catalog;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getIpAddress() {
        return ipAddress;
    }

    public void setIpAddress(String ipAddress) {
        this.ipAddress = ipAddress;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getJdbcUrl() {
        return jdbcUrl;
    }

    public void setJdbcUrl(String jdbcUrl) {
        this.jdbcUrl = jdbcUrl;
    }

    public String getJdbcServer() {
        return jdbcServer;
    }

    public void setJdbcServer(String jdbcServer) {
        this.jdbcServer = jdbcServer;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getZkAddress() {
        return zkAddress;
    }

    public void setZkAddress(String zkAddress) {
        this.zkAddress = zkAddress;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDbName() {
        return dbName;
    }

    public void setDbName(String dbName) {
        this.dbName = dbName;
    }

    public String getFolder() {
        return folder;
    }

    public void setFolder(String folder) {
        this.folder = folder;
    }

    public String getHeader() {
        return header;
    }

    public void setHeader(String header) {
        this.header = header;
    }

    public String getUrlParams() {
        return urlParams;
    }

    public void setUrlParams(String urlParams) {
        this.urlParams = urlParams;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
