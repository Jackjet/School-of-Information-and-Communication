package d1.project.caict.bigdata.goverance.tool.meta;

public class HiveDatabaseMeta extends BaseDatabaseMeta {
    private volatile static HiveDatabaseMeta single;

    public static HiveDatabaseMeta getInstance() {
        if (single == null) {
            synchronized (HiveDatabaseMeta.class) {
                if (single == null) {
                    single = new HiveDatabaseMeta();
                }
            }
        }
        return single;
    }

    @Override
    public String getSQLQueryTables(String... args) {
        return "show tables";
    }

    @Override
    public String getSQLQueryFields(String schemaName, String tableName) {
        return null;
    }
}
