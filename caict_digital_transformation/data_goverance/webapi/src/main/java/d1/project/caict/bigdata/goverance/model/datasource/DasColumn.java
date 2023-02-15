package d1.project.caict.bigdata.goverance.model.datasource;

public class DasColumn {
    private String columnName;

    private String columnTypeName;

    private String columnClassName;

    private String columnComment;
    private int isNull;
    private boolean isprimaryKey;

    public String getColumnName() {
        return columnName;
    }

    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    public String getColumnTypeName() {
        return columnTypeName;
    }

    public void setColumnTypeName(String columnTypeName) {
        this.columnTypeName = columnTypeName;
    }

    public String getColumnClassName() {
        return columnClassName;
    }

    public void setColumnClassName(String columnClassName) {
        this.columnClassName = columnClassName;
    }

    public String getColumnComment() {
        return columnComment;
    }

    public void setColumnComment(String columnComment) {
        this.columnComment = columnComment;
    }

    public int getIsNull() {
        return isNull;
    }

    public void setIsNull(int isNull) {
        this.isNull = isNull;
    }

    public boolean isIsprimaryKey() {
        return isprimaryKey;
    }

    public void setIsprimaryKey(boolean isprimaryKey) {
        this.isprimaryKey = isprimaryKey;
    }
}
