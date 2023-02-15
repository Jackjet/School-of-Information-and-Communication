package d1.project.caict.bigdata.goverance.tool.query;

//import com.mongodb.MongoClient;
//import com.mongodb.MongoClientURI;
//import com.mongodb.MongoCredential;
//import com.mongodb.ServerAddress;
//import com.mongodb.client.MongoDatabase;


public class MongoDBQueryTool {
//    private static MongoClient connection = null;
//    private static MongoDatabase collections;
//
//    public MongoDBQueryTool(JobDataSource jobDatasource) throws IOException {
//        if (LocalCacheUtil.get(jobDatasource.getName()) == null) {
//            getDataSource(jobDatasource);
//        } else {
//            connection = (MongoClient) LocalCacheUtil.get(jobDatasource.getName());
//            if (connection == null) {
//                LocalCacheUtil.remove(jobDatasource.getName());
//                getDataSource(jobDatasource);
//            }
//        }
//        LocalCacheUtil.set(jobDatasource.getName(), connection, 4 * 60 * 60 * 1000);
//    }
//
//    private void getDataSource(JobDataSource jobDatasource) throws IOException {
//        if (StringUtils.isBlank(jobDatasource.getUserName()) && StringUtils.isBlank(jobDatasource.getPassword())) {
//            connection = new MongoClient(new MongoClientURI(jobDatasource.getJdbcUrl()));
//        } else {
//            MongoCredential credential = MongoCredential.createCredential(jobDatasource.getUserName(), jobDatasource.getDbName(), jobDatasource.getPassword().toCharArray());
//            connection = new MongoClient(parseServerAddress(jobDatasource.getJdbcUrl()), Arrays.asList(credential));
//        }
//        collections = connection.getDatabase(jobDatasource.getName());
//    }
//
//    /**
//     * 测试是否连接成功
//     *
//     * @return
//     */
//    public boolean dataSourceTest(String dbName) {
//        collections = connection.getDatabase(dbName);
//        return collections.listCollectionNames().iterator().hasNext();
//    }
//
//    /**
//     * 转换为mongo地址协议
//     *
//     * @param rawAddress
//     * @return
//     */
//    private static List<ServerAddress> parseServerAddress(String rawAddress) throws UnknownHostException {
//        List<ServerAddress> addressList = new ArrayList<>();
//        for (String address : Arrays.asList(rawAddress.split(","))) {
//            String[] tempAddress = address.split(":");
//            try {
//                ServerAddress sa = new ServerAddress(tempAddress[0], Integer.valueOf(tempAddress[1]));
//                addressList.add(sa);
//            } catch (Exception e) {
//                throw new UnknownHostException();
//            }
//        }
//        return addressList;
//    }
}
