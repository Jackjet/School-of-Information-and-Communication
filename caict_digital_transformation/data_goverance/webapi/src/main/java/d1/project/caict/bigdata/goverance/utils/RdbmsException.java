package d1.project.caict.bigdata.goverance.utils;

import d1.project.caict.bigdata.goverance.Constants;

public class RdbmsException extends DataXException {
    public RdbmsException(ErrorCode errorCode, String errorMessage) {
        super(errorCode, errorMessage);
    }

    public static DataXException asConnException(String dataBaseType, Exception e, String userName, String dbName) {
        if (dataBaseType.equals(Constants.MYSQL)) {
            DBUtilErrorCode dbUtilErrorCode = mySqlConnectionErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_CONN_DB_ERROR && dbName != null) {
                return DataXException.asDataXException(dbUtilErrorCode, "该数据库名称为：" + dbName + " 具体错误信息为：" + e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.MYSQL_CONN_USERPWD_ERROR) {
                return DataXException.asDataXException(dbUtilErrorCode, "该数据库用户名为：" + userName + " 具体错误信息为：" + e);
            }
            return DataXException.asDataXException(dbUtilErrorCode, " 具体错误信息为：" + e);
        }

        if (dataBaseType.equals(Constants.ORACLE)) {
            DBUtilErrorCode dbUtilErrorCode = oracleConnectionErrorAna(e.getMessage());
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_CONN_DB_ERROR && dbName != null) {
                return DataXException.asDataXException(dbUtilErrorCode, "该数据库名称为：" + dbName + " 具体错误信息为：" + e);
            }
            if (dbUtilErrorCode == DBUtilErrorCode.ORACLE_CONN_USERPWD_ERROR) {
                return DataXException.asDataXException(dbUtilErrorCode, "该数据库用户名为：" + userName + " 具体错误信息为：" + e);
            }
            return DataXException.asDataXException(dbUtilErrorCode, " 具体错误信息为：" + e);
        }
        return DataXException.asDataXException(DBUtilErrorCode.CONN_DB_ERROR, " 具体错误信息为：" + e);
    }

    public static DBUtilErrorCode mySqlConnectionErrorAna(String e) {
        if (e.contains(Constants.MYSQL_DATABASE)) {
            return DBUtilErrorCode.MYSQL_CONN_DB_ERROR;
        }

        if (e.contains(Constants.MYSQL_CONNEXP)) {
            return DBUtilErrorCode.MYSQL_CONN_IPPORT_ERROR;
        }

        if (e.contains(Constants.MYSQL_ACCDENIED)) {
            return DBUtilErrorCode.MYSQL_CONN_USERPWD_ERROR;
        }

        return DBUtilErrorCode.CONN_DB_ERROR;
    }

    public static DBUtilErrorCode oracleConnectionErrorAna(String e) {
        if (e.contains(Constants.ORACLE_DATABASE)) {
            return DBUtilErrorCode.ORACLE_CONN_DB_ERROR;
        }

        if (e.contains(Constants.ORACLE_CONNEXP)) {
            return DBUtilErrorCode.ORACLE_CONN_IPPORT_ERROR;
        }

        if (e.contains(Constants.ORACLE_ACCDENIED)) {
            return DBUtilErrorCode.ORACLE_CONN_USERPWD_ERROR;
        }

        return DBUtilErrorCode.CONN_DB_ERROR;
    }
}
