package d1.project.caict.bigdata.goverance.tool.query;

import com.alibaba.fastjson.JSONObject;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.zaxxer.hikari.HikariDataSource;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import d1.project.caict.bigdata.goverance.model.datasource.ColumnInfo;
import d1.project.caict.bigdata.goverance.model.datasource.DasColumn;
import d1.project.caict.bigdata.goverance.tool.meta.DatabaseInterface;
import d1.project.caict.bigdata.goverance.tool.meta.DatabaseMetaFactory;
import d1.project.caict.bigdata.goverance.utils.JdbcUtils;
import d1.project.caict.bigdata.goverance.utils.LocalCacheUtil;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.*;
import java.util.*;

/**
 * @author maoyy
 */

public abstract class BaseQueryTool implements QueryToolInterface {

    protected static final Logger logger = LoggerFactory.getLogger(BaseQueryTool.class);

    /**
     * 用于获取查询语句
     */
    private DatabaseInterface sqlBuilder;

    private DataSource datasource;

    private Connection connection;
    /**
     * 当前数据库名
     */
    private String currentSchema;
    private String currentDatabase;

    /**
     * 构造方法
     *
     * @param jobDatasource
     */
    BaseQueryTool(JobDataSource jobDatasource) throws SQLException {
        if (LocalCacheUtil.get(jobDatasource.getName()) == null) {
            getDataSource(jobDatasource);
        } else {
            this.connection = (Connection) LocalCacheUtil.get(jobDatasource.getName());
            if (!this.connection.isValid(500)) {
                LocalCacheUtil.remove(jobDatasource.getName());
                getDataSource(jobDatasource);
            }
        }
        sqlBuilder = DatabaseMetaFactory.getByDbType(jobDatasource.getType());
        currentSchema = getSchema(jobDatasource.getUserName());
        currentDatabase = jobDatasource.getType();
        LocalCacheUtil.set(jobDatasource.getName(), this.connection, 4 * 60 * 60 * 1000);
    }

    private void getDataSource(JobDataSource jobDatasource) throws SQLException {
        String userName = jobDatasource.getUserName();

        //这里默认使用 hikari 数据源
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setUsername(userName);
        dataSource.setPassword(jobDatasource.getPassword());
        dataSource.setJdbcUrl(jobDatasource.getJdbcUrl());
        dataSource.setDriverClassName(jobDatasource.getJdbcServer());
        dataSource.setMaximumPoolSize(1);
        dataSource.setMinimumIdle(0);
        dataSource.setConnectionTimeout(30000);
        this.datasource = dataSource;
        this.connection = this.datasource.getConnection();
    }

    //根据connection获取schema
    private String getSchema(String jdbcUsername) {
        String res = null;
        try {
            res = connection.getCatalog();
        } catch (SQLException e) {
            try {
                res = connection.getSchema();
            } catch (SQLException e1) {
                logger.error("[SQLException getSchema Exception] --> " + "the exception message is:" + e1.getMessage());
            }
            logger.error("[getSchema Exception] --> " + "the exception message is:" + e.getMessage());
        }
        // 如果res是null，则将用户名当作 schema
        if (StringUtils.isBlank(res) && StringUtils.isNotBlank(jdbcUsername)) {
            res = jdbcUsername.toUpperCase();
        }
        return res;
    }

    public Boolean dataSourceTest() {
        try {
            DatabaseMetaData metaData = connection.getMetaData();
            if (metaData.getDatabaseProductName().length() > 0) {
                return true;
            }
        } catch (SQLException e) {
            logger.error("[dataSourceTest Exception] --> " + "the exception message is:" + e.getMessage());
        }
        return false;
    }

    /**
     * 获取所有表名
     *
     * @return
     */
    public List<String> getTableSchema() {
        List<String> schemas = new ArrayList<>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSQLQueryTableSchema();
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                schemas.add(tableName);
            }
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> " + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return schemas;
    }

    @Override
    public MetaDataTable buildTableInfo(String tableName) {
        //获取表信息
        List<Map<String, Object>> tableInfos = this.getTableInfo(tableName);
        if (tableInfos.isEmpty()) {
            throw new NullPointerException("查询出错! ");
        }

        MetaDataTable tableInfo = new MetaDataTable();
        //表名，注释
        List tValues = new ArrayList(tableInfos.get(0).values());

        tableInfo.setTableName(tValues.get(0).toString());
        tableInfo.setRemark(tValues.get(1).toString());

        //获取所有字段
//        List<ColumnInfo> fullColumn = getColumns(tableName);
//        tableInfo.setColumns(fullColumn);

        //获取主键列
//        List<String> primaryKeys = getPrimaryKeys(tableName);
//        logger.info("主键列为：{}", primaryKeys);

        //设置ifPrimaryKey标志
//        fullColumn.forEach(e -> {
//            if (primaryKeys.contains(e.getName())) {
//                e.setIfPrimaryKey(true);
//            } else {
//                e.setIfPrimaryKey(false);
//            }
//        });
        return tableInfo;
    }

    //无论怎么查，返回结果都应该只有表名和表注释，遍历map拿value值即可
    @Override
    public List<Map<String, Object>> getTableInfo(String tableName) {
        String sqlQueryTableNameComment = sqlBuilder.getSQLQueryTableNameComment();
        logger.info(sqlQueryTableNameComment);
        List<Map<String, Object>> res = null;
        try {
            res = JdbcUtils.executeQuery(connection, sqlQueryTableNameComment, ImmutableList.of(currentSchema, tableName));
        } catch (SQLException e) {
            logger.error("[getTableInfo Exception] --> " + "the exception message is:" + e.getMessage());
        }
        return res;
    }

    @Override
    public List<ColumnInfo> getColumns(String jdbcUserName, String tableName) {

        List<ColumnInfo> fullColumn = Lists.newArrayList();
        //获取指定表的所有字段
        try {
            //获取查询指定表所有字段的sql语句
            String schemaName = getSchema(jdbcUserName);
            String querySql = sqlBuilder.getSQLQueryFields(schemaName, tableName);
            logger.info("querySql: {}", querySql);

            //获取所有字段
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(querySql);

            while (resultSet.next()) {
                String columnName = resultSet.getString("column_name");
                String isNullable = resultSet.getString("is_nullable");
                String columnType = "";
                boolean columnKey = false;
                String columnComment = "";
                if (currentDatabase.equals(Constants.MYSQL)) {
                    columnType = resultSet.getString("column_type");
                    columnKey = resultSet.getString("column_key").equals("PRI") ? true : false;
                    columnComment = resultSet.getString("column_comment");
                } else if (currentDatabase.equals(Constants.POSTGRESQL)) {
                    columnType = resultSet.getString("udt_name");
                    columnKey = resultSet.getString("is_identity").equals("YES") ? true : false;
                }

                ColumnInfo columnInfo = new ColumnInfo();
                columnInfo.setName(columnName);
                columnInfo.setIsnull(isNullable.equals("YES") ? 1 : 0);
                columnInfo.setIfPrimaryKey(columnKey);
                columnInfo.setType(columnType);
                columnInfo.setComment(columnComment);
                fullColumn.add(columnInfo);
            }

            statement.close();

        } catch (SQLException e) {
            logger.error("[getColumns Exception] --> " + "the exception message is:" + e.getMessage());
        }
        return fullColumn;
    }

    private List<ColumnInfo> buildFullColumn(List<DasColumn> dasColumns) {
        List<ColumnInfo> res = Lists.newArrayList();
        dasColumns.forEach(e -> {
            ColumnInfo columnInfo = new ColumnInfo();
            columnInfo.setName(e.getColumnName());
            columnInfo.setComment(e.getColumnComment());
            columnInfo.setType(e.getColumnTypeName());
            columnInfo.setIfPrimaryKey(e.isIsprimaryKey());
            columnInfo.setIsnull(e.getIsNull());
            res.add(columnInfo);
        });
        return res;
    }

    //构建DasColumn对象
    private List<DasColumn> buildDasColumn(String tableName, ResultSetMetaData metaData) {
        List<DasColumn> res = Lists.newArrayList();
        try {
            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                DasColumn dasColumn = new DasColumn();
                dasColumn.setColumnClassName(metaData.getColumnClassName(i));
                dasColumn.setColumnTypeName(metaData.getColumnTypeName(i));
                dasColumn.setColumnName(metaData.getColumnName(i));
                dasColumn.setIsNull(metaData.isNullable(i));

                res.add(dasColumn);
            }

            Statement statement = connection.createStatement();

            if (currentDatabase.equals(Constants.MYSQL) || currentDatabase.equals(Constants.ORACLE)) {
                DatabaseMetaData databaseMetaData = connection.getMetaData();

                ResultSet resultSet = databaseMetaData.getPrimaryKeys(null, null, tableName);

                while (resultSet.next()) {
                    String name = resultSet.getString("COLUMN_NAME");
                    res.forEach(e -> {
                        if (e.getColumnName().equals(name)) {
                            e.setIsprimaryKey(true);

                        } else {
                            e.setIsprimaryKey(false);
                        }
                    });
                }

                res.forEach(e -> {
                    String sqlQueryComment = sqlBuilder.getSQLQueryComment(currentSchema, tableName, e.getColumnName());
                    //查询字段注释
                    try {
                        ResultSet resultSetComment = statement.executeQuery(sqlQueryComment);
                        while (resultSetComment.next()) {
                            e.setColumnComment(resultSetComment.getString(1));
                        }
                        JdbcUtils.close(resultSetComment);
                    } catch (SQLException e1) {
                        logger.error("[buildDasColumn executeQuery Exception] --> " + "the exception message is:" + e1.getMessage());
                    }
                });
            }

            JdbcUtils.close(statement);
        } catch (SQLException e) {
            logger.error("[buildDasColumn Exception] --> " + "the exception message is:" + e.getMessage());
        }
        return res;
    }


//    @Override
//    public List<String> getColumnNames(String tableName, String datasource) {
//        List<String> res = Lists.newArrayList();
//        Statement stmt = null;
//        ResultSet rs = null;
//        try {
//            //获取查询指定表所有字段的sql语句
//            String querySql = sqlBuilder.getSQLQueryFields(tableName);
//            logger.info("querySql: {}", querySql);
//
//            //获取所有字段
//            stmt = connection.createStatement();
//            rs = stmt.executeQuery(querySql);
//            ResultSetMetaData metaData = rs.getMetaData();
//
//            int columnCount = metaData.getColumnCount();
//            for (int i = 1; i <= columnCount; i++) {
//                String columnName = metaData.getColumnName(i);
//                if (Constants.HIVE.equals(datasource)) {
//                    if (columnName.contains(Constants.SPLIT_POINT)) {
//                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName.substring(columnName.indexOf(Constants.SPLIT_POINT) + 1) + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
//                    } else {
//                        res.add(i - 1 + Constants.SPLIT_SCOLON + columnName + Constants.SPLIT_SCOLON + metaData.getColumnTypeName(i));
//                    }
//                } else {
//                    res.add(columnName);
//                }
//
//            }
//        } catch (SQLException e) {
//            logger.error("[getColumnNames Exception] --> "
//                    + "the exception message is:" + e.getMessage());
//        } finally {
//            JdbcUtils.close(rs);
//            JdbcUtils.close(stmt);
//        }
//        return res;
//    }

    @Override
    public List<String> getTableNames(String tableSchema) {
        List<String> tables = new ArrayList<String>();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = getSQLQueryTables(tableSchema);
            rs = stmt.executeQuery(sql);
            while (rs.next()) {
                String tableName = rs.getString(1);
                tables.add(tableName);
            }
            tables.sort(Comparator.naturalOrder());
        } catch (SQLException e) {
            logger.error("[getTableNames Exception] --> " + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return tables;
    }

    /**
     * 不需要其他参数的可不重写
     *
     * @return
     */
    protected String getSQLQueryTables(String tableSchema) {
        return sqlBuilder.getSQLQueryTables(tableSchema);
    }

    @Override
    public List<String> getColumnsByQuerySql(String querySql) throws SQLException {
        List<String> res = Lists.newArrayList();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            querySql = querySql.replace(";", "");
            //拼装sql语句，在后面加上 where 1=0 即可
            String sql = querySql.concat(" where 1=0");
            //判断是否已有where，如果是，则加 and 1=0 从最后一个 ) 开始找 where，或者整个语句找
            if (querySql.contains(")")) {
                if (querySql.substring(querySql.indexOf(")")).contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            } else {
                if (querySql.contains("where")) {
                    sql = querySql.concat(" and 1=0");
                }
            }
            //获取所有字段
            stmt = connection.createStatement();
            rs = stmt.executeQuery(sql);
            ResultSetMetaData metaData = rs.getMetaData();

            int columnCount = metaData.getColumnCount();
            for (int i = 1; i <= columnCount; i++) {
                res.add(metaData.getColumnName(i));
            }
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return res;
    }

    public void executeCreateTableSql(String querySql) {
        if (MyUtils.isEmpty(querySql)) {
            return;
        }
        Statement stmt = null;
        try {
            stmt = connection.createStatement();
            stmt.executeUpdate(querySql);
        } catch (SQLException e) {
            logger.error("[executeCreateTableSql Exception] --> " + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(stmt);
        }
    }

    protected String getSQLQueryTableSchema() {
        return sqlBuilder.getSQLQueryTableSchema();
    }

    @Override
    public JSONObject getTableDataWithPaging(String tableName, Integer pageIndex, Integer pageSize) {
        JSONObject result = new JSONObject();
        Statement stmt = null;
        ResultSet rs = null;
        try {
            pageIndex = (pageIndex == null || pageIndex < 1) ? 0 : pageIndex - 1;
            if (pageSize == null || pageSize < 1) {
                pageSize = 10;
            }
            stmt = connection.createStatement();
            //获取sql
            String sql = sqlBuilder.getSQLQueryDataWithPaging(tableName, pageIndex, pageSize);
            rs = stmt.executeQuery(sql);
            //获取键名
            ResultSetMetaData md = rs.getMetaData();
            //获取行的数量
            int columnCount = md.getColumnCount();
            List<Map<String, Object>> data = new ArrayList<>();
            while (rs.next()) {
                Map<String, Object> rowData = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    rowData.put(md.getColumnName(i), rs.getObject(i));
                }
                data.add(rowData);
            }

            long totalCount = getCount(tableName);
            int totalPages = (int) Math.ceil((double) totalCount / (double) pageSize);

            result.put("content", data);
            result.put("totalElements", totalCount);
            result.put("totalPages", totalPages);
        } catch (SQLException e) {
            logger.error("[getTableDataWithPaging Exception] --> " + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return result;
    }


    @Override
    public long getCount(String tableName) {
        Statement stmt = null;
        ResultSet rs = null;
        try {
            stmt = connection.createStatement();
            //获取sql
            String sql = "select count(1) from " + tableName;
            rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getLong(1);
            }
        } catch (SQLException e) {
            logger.error("[getCount Exception] --> " + "the exception message is:" + e.getMessage());
        } finally {
            JdbcUtils.close(rs);
            JdbcUtils.close(stmt);
        }
        return 0;
    }
}
