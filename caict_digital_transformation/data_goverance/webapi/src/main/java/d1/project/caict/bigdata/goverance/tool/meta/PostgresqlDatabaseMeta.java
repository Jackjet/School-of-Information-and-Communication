package d1.project.caict.bigdata.goverance.tool.meta;

public class PostgresqlDatabaseMeta extends BaseDatabaseMeta {
    private volatile static PostgresqlDatabaseMeta single;

    public static PostgresqlDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (PostgresqlDatabaseMeta.class) {
                if (single == null) {
                    single = new PostgresqlDatabaseMeta();
                }
            }
        }
        return single;
    }

    @Override
    public String getSQLQueryPrimaryKey() {
        return "select column_name from information_schema.columns where table_schema='public' and table_name='tb_cis_patient_info' and is_identity = 'YES'";
    }

    @Override
    public String getSQLQueryTables(String... args) {
        return "select tablename from pg_tables where schemaname='public'";
    }

    @Override
    public String getSQLQueryTableSchema(String... args) {
        return "SELECT tablename FROM pg_tables WHERE tablename NOT LIKE 'pg%' AND tablename NOT LIKE 'sql_%' ORDER BY tablename;";
    }

    @Override
    public String getSQLQueryColumns(String... args) {
        return "SELECT a.attname as name \n" +
                "FROM pg_class as c,pg_attribute as a where c.relname = ? and a.attrelid = c.oid and a.attnum>0";
    }

    @Override
    public String getSQLQueryComment(String schemaName, String tableName, String columnName) {
        return null;
    }

    @Override
    public String getSQLQueryFields(String schemaName, String tableName) {
        return String.format("select * from information_schema.columns where table_schema='public' and table_name='%s'", tableName);
    }

    @Override
    public String getSQLQueryDataWithPaging(String tableName, int pageIndex, int pageSize) {
        return "select * from " + tableName + " limit " + pageSize + " offset " + pageIndex * pageSize;
    }
}
