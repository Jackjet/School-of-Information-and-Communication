package d1.project.caict.bigdata.goverance.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.datasource.JobDataSourceDao;
import d1.project.caict.bigdata.goverance.dao.datasource.MetaDataTableDao;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import d1.project.caict.bigdata.goverance.service.datasource.JobDataSourceService;
import d1.project.caict.bigdata.goverance.service.datasource.MetaDataTableService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author maoyy
 */

@Service
public class ResourceLibraryService {
    private final JobDataSourceService jobDataSourceService;
    private final MetaDataTableService metaDataTableService;

    public ResourceLibraryService(JobDataSourceService jobDataSourceService, MetaDataTableService metaDataTableService) {
        this.jobDataSourceService = jobDataSourceService;
        this.metaDataTableService = metaDataTableService;
    }


    /**
     * 查询数据库下面的所有表
     *
     * @param params
     * @return
     */
    public Page<MetaDataTable> findAllTableWithDbName(JSONObject params) throws Exception {
        String dbName = params.getString("dbName");
        JobDataSource jobDataSource = jobDataSourceService.findFirstByDbName(dbName).orElseThrow(() -> new ValidException("找不到记录:" + dbName));
        params.put("dataSourceId", jobDataSource.getId());
        return metaDataTableService.findAll(params);
    }

    /**
     * 查询表的数据
     *
     * @param params
     * @return
     */
    public JSONObject findAllDataWithDbNameAndTableName(JSONObject params) throws Exception {
        String dbName = params.getString("dbName");
        JobDataSource jobDataSource = jobDataSourceService.findFirstByDbName(dbName).orElseThrow(() -> new ValidException("找不到记录:" + dbName));

        String dataSourceId = jobDataSource.getId();
        String tableName = params.getString("tableName");
        String id = metaDataTableService.findFirstIdWithDataSourceIdAndTableName(dataSourceId, tableName);

        SpecificationBuilder<MetaDataTable> builder = new SpecificationBuilder<>();
        builder.init(params);
        Pageable pageable = builder.getPageable();
        return this.metaDataTableService.findAllTableData(id, pageable.getPageNumber(), pageable.getPageSize());
    }
}
