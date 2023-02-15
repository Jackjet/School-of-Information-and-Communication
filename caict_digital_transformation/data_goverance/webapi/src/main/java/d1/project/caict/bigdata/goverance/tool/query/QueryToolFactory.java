package d1.project.caict.bigdata.goverance.tool.query;

import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.utils.RdbmsException;

import java.sql.SQLException;

/**
 * @author maoyy
 */

public class QueryToolFactory {
    public static final BaseQueryTool getByDbType(JobDataSource jobDatasource) {
        //获取dbType
        String datasource = jobDatasource.getType();
        if (Constants.MYSQL.equals(datasource)) {
            return getMySQLQueryToolInstance(jobDatasource);
        } else if (Constants.ORACLE.equals(datasource)) {
            return getOracleQueryToolInstance(jobDatasource);
        } else if (Constants.POSTGRESQL.equals(datasource)) {
            return getPostgresqlQueryToolInstance(jobDatasource);
        } else if (Constants.SQL_SERVER.equals(datasource)) {
            return getSqlserverQueryToolInstance(jobDatasource);
        } else if (Constants.HIVE.equals(datasource)) {
            return getHiveQueryToolInstance(jobDatasource);
        }
        throw new UnsupportedOperationException("找不到该类型: ".concat(datasource));
    }

    private static BaseQueryTool getMySQLQueryToolInstance(JobDataSource jdbcDatasource) {
        try {
            return new MySQLQueryTool(jdbcDatasource);
        } catch (Exception e) {
            throw RdbmsException.asConnException(Constants.MYSQL, e, jdbcDatasource.getUserName(), jdbcDatasource.getName());
        }
    }

    private static BaseQueryTool getOracleQueryToolInstance(JobDataSource jdbcDatasource) {
        try {
            return new OracleQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(Constants.ORACLE, e, jdbcDatasource.getUserName(), jdbcDatasource.getName());
        }
    }

    private static BaseQueryTool getPostgresqlQueryToolInstance(JobDataSource jdbcDatasource) {
        try {
            return new PostgresqlQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(Constants.POSTGRESQL, e, jdbcDatasource.getUserName(), jdbcDatasource.getName());
        }
    }

    private static BaseQueryTool getSqlserverQueryToolInstance(JobDataSource jdbcDatasource) {
        try {
            return new SqlServerQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(Constants.SQL_SERVER, e, jdbcDatasource.getUserName(), jdbcDatasource.getName());
        }
    }

    private static BaseQueryTool getHiveQueryToolInstance(JobDataSource jdbcDatasource) {
        try {
            return new HiveQueryTool(jdbcDatasource);
        } catch (SQLException e) {
            throw RdbmsException.asConnException(Constants.HIVE, e, jdbcDatasource.getUserName(), jdbcDatasource.getName());
        }
    }
}
