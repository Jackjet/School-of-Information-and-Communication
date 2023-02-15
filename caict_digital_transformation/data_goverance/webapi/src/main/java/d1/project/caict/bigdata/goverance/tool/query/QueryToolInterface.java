package d1.project.caict.bigdata.goverance.tool.query;

import com.alibaba.fastjson.JSONObject;
import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import d1.project.caict.bigdata.goverance.model.datasource.ColumnInfo;

import java.sql.SQLException;
import java.util.List;
import java.util.Map;

public interface QueryToolInterface {
    /**
     * 构建 tableInfo对象
     *
     * @param tableName 表名
     * @return
     */
    MetaDataTable buildTableInfo(String tableName);

    /**
     * 获取指定表信息
     *
     * @return
     */
    List<Map<String, Object>> getTableInfo(String tableName);

    /**
     * 获取当前schema下的所有表
     *
     * @return
     */
//    List<Map<String, Object>> getTables();

    /**
     * 根据表名获取所有字段
     *
     * @param jdbcUserName
     * @param tableName
     * @return
     */
    List<ColumnInfo> getColumns(String jdbcUserName, String tableName);


    /**
     * 根据表名和获取所有字段名称（不包括表名）
     *
     * @param tableName
     * @return2
     */
//    List<String> getColumnNames(String tableName, String datasource);


    /**
     * 获取所有可用表名
     *
     * @return2
     */
    List<String> getTableNames(String schema);

    /**
     * 通过查询sql获取columns
     *
     * @param querySql
     * @return
     */
    List<String> getColumnsByQuerySql(String querySql) throws SQLException;

    /**
     * 查询表数据带分页
     */
    JSONObject getTableDataWithPaging(String tableName, Integer pageIndex, Integer pageSize);

    /**
     * 查询表的数据数量
     */
    long getCount(String tableName);
}
