package d1.project.caict.bigdata.goverance.tool.query;

import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;

import java.sql.SQLException;

public class PostgresqlQueryTool extends BaseQueryTool {
    public PostgresqlQueryTool(JobDataSource jobDatasource) throws SQLException {
        super(jobDatasource);
    }
}
