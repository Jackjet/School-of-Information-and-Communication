package d1.project.caict.bigdata.goverance.tool.query;

import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;

import java.sql.SQLException;

/**
 * @author maoyy
 */

public class MySQLQueryTool extends BaseQueryTool {
    public MySQLQueryTool(JobDataSource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
