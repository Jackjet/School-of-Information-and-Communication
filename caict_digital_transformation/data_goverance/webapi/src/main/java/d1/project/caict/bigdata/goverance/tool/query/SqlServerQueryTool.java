package d1.project.caict.bigdata.goverance.tool.query;

import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;

import java.sql.SQLException;

public class SqlServerQueryTool extends BaseQueryTool {
    public SqlServerQueryTool(JobDataSource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
