package d1.project.caict.bigdata.goverance;

import java.io.File;

/**
 * 一些常量，包括枚举等
 *
 * @author Buter
 * @date 2020/3/18 16:09
 */
public class Constants {

    public static final String SUCCESS = "成功";
    public static final String SYNCHRONOUS_SUCCESS = "同步成功";
    public static final String ADD_SUCCESS = "添加成功";
    public static final String UPDATE_SUCCESS = "编辑成功";
    public static final String DELETE_SUCCESS = "删除成功";
    public static final String IMPORT_SUCCESS = "导入成功";
    public static final String OPERATION_SUCCESS = "操作成功";

    public static final String HBASE = "Hbase";
    public static final String MONGODB = "MongoDB";
    public static final String MYSQL = "MySQL";
    public static final String ORACLE = "Oracle";
    public static final String POSTGRESQL = "PostgreSQL";
    public static final String SQL_SERVER = "SQLServer";
    public static final String HIVE = "Hive";
    public static final String ELASTICSEARCH = "ElasticSearch";
    public static final String HTTP = "HTTP";
    public static final String FTP = "FTP";
    public static final String HDFS = "HDFS";

    //TODO 赋值
    public static final String MYSQL_DATABASE = "Unknown database";
    public static final String MYSQL_CONNEXP = "Communications link failure";
    public static final String MYSQL_ACCDENIED = "Access denied";
    public static final String MYSQL_TABLE_NAME_ERR1 = "Table";
    public static final String MYSQL_TABLE_NAME_ERR2 = "doesn't exist";
    public static final String MYSQL_SELECT_PRI = "SELECT command denied to user";
    public static final String MYSQL_COLUMN1 = "Unknown column";
    public static final String MYSQL_COLUMN2 = "field list";
    public static final String MYSQL_WHERE = "where clause";
    public static final String ORACLE_DATABASE = "ORA-12505";
    public static final String ORACLE_CONNEXP = "The Network Adapter could not establish the connection";
    public static final String ORACLE_ACCDENIED = "ORA-01017";
    public static final String ORACLE_TABLE_NAME = "table or view does not exist";
    public static final String ORACLE_SELECT_PRI = "insufficient privileges";
    public static final String ORACLE_SQL = "invalid identifier";

    public static final String SPLIT_COMMA = ",";
    public static final String SPLIT_AT = "@";
    public static final String SPLIT_COLON = ";";
    public static final String SPLIT_POINT = ".";
    public static final String SPLIT_SCOLON=":";
    public static final String SPLIT_HYPHEN = "-";
    public static final String SPLIT_DIVIDE = "/";
    public static final String SPLIT_STAR = "*";
    public static final String SPLIT_QUESTION = "?";
    public static final String EQUAL = "=";
    public static final String SPLIT_AMPERSAND = "&";
    public static final String AND = "AND";
    public static final String SPACE = " ";
    public static final String STRING_BLANK = "";
    public static final String MONGO_URL_PREFIX = "mongodb://";

    /**
     * 用户登录后token失效时间，暂定1天
     */
    public final static int TOKEN_EXPIRE_TIME = 1 * 24 * 60 * 60;

    public final static String FILES_ROOT = "." + File.separator + "files" + File.separator;

    public final static String FILES_STORAGE = FILES_ROOT + "storage" + File.separator;

    public final static String FILES_REGULATION = FILES_STORAGE + "regulation" + File.separator;
    public final static String FILES_KNOWLEDGE = FILES_STORAGE + "knowledge" + File.separator;
}
