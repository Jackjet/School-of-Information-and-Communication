package d1.project.caict.bigdata.goverance.service.datasource;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.caict.bigdata.goverance.dao.datasource.MetaDataTableDao;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import d1.project.caict.bigdata.goverance.model.datasource.ColumnInfo;
import d1.project.caict.bigdata.goverance.tool.query.BaseQueryTool;
import d1.project.caict.bigdata.goverance.tool.query.QueryToolFactory;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * @author maoyy
 */

@Service
public class MetaDataTableService {
    private final MetaDataTableDao metaDataTableDao;
    private final TokenService tokenService;
    private final JobDataSourceService jobDataSourceService;

    @Autowired
    public MetaDataTableService(MetaDataTableDao metaDataTableDao, TokenService tokenService, JobDataSourceService jobDataSourceService) {
        this.metaDataTableDao = metaDataTableDao;
        this.tokenService = tokenService;
        this.jobDataSourceService = jobDataSourceService;
    }

    /**
     * 查询数据源下的元数据
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<MetaDataTable> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<MetaDataTable> builder = new SpecificationBuilder<>();
        Specification<MetaDataTable> specification = builder.init(params)
                .sEqual("dataSourceId", "dataSourceId")
                .dOrder("createTime").build();

        return this.metaDataTableDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询数据源详细信息
     *
     * @param id
     * @return
     */
    public Optional<MetaDataTable> findById(String id) {
        return metaDataTableDao.findById(id);
    }

    public String findFirstIdWithDataSourceIdAndTableName(String dataSourceId, String tableName) throws ValidException {
        List<String> ids = metaDataTableDao.findFirstIdWithDataSourceIdAndTableName(dataSourceId, tableName);
        if (ids == null || ids.size() == 0) {
            throw new ValidException("找不到对应“" + tableName + "”表数据");
        }
        return ids.get(0);
    }

    /**
     * 更新数据源元数据
     *
     * @throws Exception
     */
    @Transactional(rollbackFor = Exception.class)
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        String dataSourceId = params.getString("dataSourceId");
        JobDataSource jobDataSource = jobDataSourceService.findById(dataSourceId).orElseThrow(() -> new ValidException("未找到数据源信息:" + dataSourceId));
        //查询到连接数据库信息,查询库下所有表信息，循环插入元数据表中
        String userName = jobDataSource.getUserName();
        if (userName == null) {
            throw new ValidException("用户名不能为空");
        }
        String pwd = jobDataSource.getPassword();
        if (pwd == null) {
            throw new ValidException("密码不能为空");
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDataSource);
        //从数据库查询最新的数据
        List<String> newTables = queryTool.getTableSchema();
        //从缓存中查询数据
        List<String> oldTables = metaDataTableDao.findTableNameByDataSourceId(jobDataSource.getId());
        //比较两个数据，获取新增的表
        List<String> addTables = MyUtils.getAddaListThanbList(newTables, oldTables);
        List<MetaDataTable> metaDataTables = new ArrayList<>();
        for (String tableName : addTables) {
            MetaDataTable metaDataTable = new MetaDataTable();
            metaDataTable.setId(MyUtils.generate32Id());
            metaDataTable.setDataSourceId(jobDataSource.getId());
            metaDataTable.setDataSourceName(jobDataSource.getName());
            metaDataTable.setTableName(tableName == null ? "" : tableName);
            metaDataTable.setStatus("已更新");
            tokenService.updateCreateIdAndTime(request, metaDataTable);
            metaDataTables.add(metaDataTable);
        }

        if (addTables.size() > 0) {
            metaDataTableDao.saveAll(metaDataTables);
        }

        //比较两个数据，获取删除的表
        List<String> delTables = MyUtils.getReduceaListThanbList(newTables, oldTables);
        if (delTables.size() > 0) {
            metaDataTableDao.deleteByDataSourceIdAndTableName(jobDataSource.getId(), delTables);
        }
    }

    /**
     * 更新数据源元数据字段
     *
     * @param request
     * @throws Exception
     */
    public void updateFields(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        MetaDataTable entity = metaDataTableDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        //查询表所对应的表字段，转化为json数组，重新储存在tableFields字段中
        JobDataSource jobDataSource = jobDataSourceService.findById(entity.getDataSourceId()).orElseThrow(() -> new ValidException("未找到数据源信息:" + entity.getDataSourceId()));
        //查询到连接数据库信息,查询库下所有表信息，循环插入元数据表中
        String userName = jobDataSource.getUserName();
        if (userName == null) {
            throw new ValidException("用户名不能为空");
        }
        String pwd = jobDataSource.getPassword();
        if (pwd == null) {
            throw new ValidException("密码不能为空");
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDataSource);
        List<ColumnInfo> list = queryTool.getColumns(jobDataSource.getUserName(), entity.getTableName());
        String str = JSONArray.toJSONString(list);
        entity.setTableFields(str);
        tokenService.updateUpdateIdAndTime(request, entity);
        metaDataTableDao.save(entity);
    }

    public JSONObject findAllTableData(String id, Integer page, Integer size) throws Exception {
        MetaDataTable entity = metaDataTableDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        //查询表所对应的表字段，转化为json数组，重新储存在tableFields字段中
        JobDataSource jobDataSource = jobDataSourceService.findById(entity.getDataSourceId()).orElseThrow(() -> new ValidException("未找到数据源信息:" + entity.getDataSourceId()));
        //查询到连接数据库信息,查询库下所有表信息，循环插入元数据表中
        String userName = jobDataSource.getUserName();
        if (userName == null) {
            throw new ValidException("用户名不能为空");
        }
        String pwd = jobDataSource.getPassword();
        if (pwd == null) {
            throw new ValidException("密码不能为空");
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDataSource);
        return queryTool.getTableDataWithPaging(entity.getTableName(), page, size);
    }
}

