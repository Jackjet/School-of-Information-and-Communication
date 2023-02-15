package d1.project.caict.bigdata.goverance.tool.query;


//import org.apache.hadoop.conf.Configuration;
//import org.apache.hadoop.hbase.HBaseConfiguration;
//import org.apache.hadoop.hbase.HTableDescriptor;
//import org.apache.hadoop.hbase.client.Admin;
//import org.apache.hadoop.hbase.client.Connection;
//import org.apache.hadoop.hbase.client.ConnectionFactory;


public class HBaseQueryTool {
//    private Configuration conf = HBaseConfiguration.create();
//    private ExecutorService pool = Executors.newScheduledThreadPool(2);
//    private Connection connection = null;
//    private Admin admin;
//
//    public HBaseQueryTool(JobDataSource jobDatasource) throws IOException {
//        if (LocalCacheUtil.get(jobDatasource.getName()) == null) {
//            getDataSource(jobDatasource);
//        } else {
//            connection = (Connection) LocalCacheUtil.get(jobDatasource.getName());
//            if (connection == null || connection.isClosed()) {
//                LocalCacheUtil.remove(jobDatasource.getName());
//                getDataSource(jobDatasource);
//            }
//        }
//        LocalCacheUtil.set(jobDatasource.getName(), connection, 4 * 60 * 60 * 1000);
//    }
//
//    private void getDataSource(JobDataSource jobDatasource) throws IOException {
//        //TODO Constants.SPLIT_SCOLON 未赋值
//        String[] zkAdress = jobDatasource.getZkAddress().split(Constants.SPLIT_SCOLON);
//        conf.set("hbase.zookeeper.quorum", zkAdress[0]);
//        conf.set("hbase.zookeeper.property.clientPort", zkAdress[1]);
//        connection = ConnectionFactory.createConnection(conf, pool);
//        admin = connection.getAdmin();
//    }
//
//    /**
//     * 测试是否连接成功
//     *
//     * @return
//     * @throws IOException
//     */
//    public boolean dataSourceTest() throws IOException {
//        Admin admin = connection.getAdmin();
//        HTableDescriptor[] tableDescriptor = admin.listTables();
//        return tableDescriptor.length > 0;
//    }
}
