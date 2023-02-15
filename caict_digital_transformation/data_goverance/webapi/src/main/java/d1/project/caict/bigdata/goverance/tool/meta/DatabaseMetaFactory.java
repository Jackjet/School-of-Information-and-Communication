package d1.project.caict.bigdata.goverance.tool.meta;

import d1.project.caict.bigdata.goverance.Constants;

/**
 * @author maoyy
 */

public class DatabaseMetaFactory {
    /**
     * 根据数据库类型返回对应的接口
     * @param dbType
     * @return
     */
    public static DatabaseInterface getByDbType(String dbType) {
        if (Constants.MYSQL.equals(dbType)) {
            return MySQLDatabaseMeta.getInstance();
        } else if (Constants.ORACLE.equals(dbType)) {
            return OracleDatabaseMeta.getInstance();
        } else if (Constants.POSTGRESQL.equals(dbType)) {
            return PostgresqlDatabaseMeta.getInstance();
        } else if (Constants.SQL_SERVER.equals(dbType)) {
            return SqlServerDatabaseMeta.getInstance();
        } else if (Constants.HIVE.equals(dbType)) {
            return HiveDatabaseMeta.getInstance();
        } else {
            throw new UnsupportedOperationException("暂不支持的类型：".concat(dbType));
        }
    }
}
