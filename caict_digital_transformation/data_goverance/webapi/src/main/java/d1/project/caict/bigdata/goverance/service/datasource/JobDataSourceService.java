package d1.project.caict.bigdata.goverance.service.datasource;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.framework.webapi.utils.TokenService;
import d1.project.caict.bigdata.goverance.Constants;
import d1.project.caict.bigdata.goverance.dao.datasource.JobDataSourceDao;
import d1.project.caict.bigdata.goverance.dao.task.GroupTaskDao;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.datasource.JobDataSource;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.tool.query.BaseQueryTool;
import d1.project.caict.bigdata.goverance.tool.query.QueryToolFactory;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Optional;

/**
 * @author maoyy
 */

@Service
public class JobDataSourceService {
    private final JobDataSourceDao jobDataSourceDao;
    private final OperationLogService operationLogService;
    private final TokenService tokenService;
    private final WebAdminService webAdminService;
    private final GroupTaskDao groupTaskDao;

    public JobDataSourceService(WebAdminService webAdminService, TokenService tokenService, OperationLogService operationLogService, JobDataSourceDao jobDataSourceDao, GroupTaskDao groupTaskDao) {
        this.webAdminService = webAdminService;
        this.tokenService = tokenService;
        this.operationLogService = operationLogService;
        this.jobDataSourceDao = jobDataSourceDao;
        this.groupTaskDao = groupTaskDao;
    }


    /**
     * 查询数据源表信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<JobDataSource> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<JobDataSource> builder = new SpecificationBuilder<>();
        Specification specification = builder.init(params)
                .sEqual("catalog", "catalog")
                .sEqual("type", "type")
                .sEqual("status", "status")
                .sContain("name", "name")
                .dOrder("createTime").build();

        return this.jobDataSourceDao.findAll(specification, builder.getPageable());
    }

    /**
     * 查询数据源详细信息
     *
     * @param id
     * @return
     */
    public Optional<JobDataSource> findById(String id) {
        return jobDataSourceDao.findById(id);
    }

    public Optional<JobDataSource> findFirstByDbName(String dbName) {
        return jobDataSourceDao.findFirstByDbName(dbName);
    }

    /**
     * 添加数据源数据
     */
    public void insert(JSONObject params, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        //同一数据源分类、同一数据源类型下名称不能重复
        JobDataSource jobDataSource = jobDataSourceDao.findFirstByNameAndCatalogAndType(params.getString("name"), params.getString("catalog"), params.getString("type"));
        if (jobDataSource != null) {
            throw new ValidException("同一数据源分类、同一数据源类型下名称不能重复");
        }

        try {
            JobDataSource entity = JSON.toJavaObject(params, JobDataSource.class);
            String jdbcUrl = entity.getJdbcUrl();
            if (StringUtils.isEmpty(jdbcUrl)) {
                throw new ValidException("数据源jdbc连接字符串不能为空");
            }
            if (!jdbcUrl.contains("/")) {
                throw new ValidException("数据源jdbc连接字符串格式不对");
            }
            entity.setId(MyUtils.generate32Id());
            entity.setStatus("生效");
            entity.setDbName(jdbcUrl.substring(jdbcUrl.lastIndexOf('/') + 1));
            tokenService.updateCreateIdAndTime(request, entity);
            jobDataSourceDao.save(entity);
            operationLogService.insert(new OperationLog("数据源管理—添加", "数据源管理", "添加了数据源名称为:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("数据源管理—添加", "数据源管理", "添加了数据源名称为:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 修改数据源数据
     */
    public void update(JSONObject params, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebAdminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String id = params.getString("id");
        JobDataSource entity = jobDataSourceDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        try {
            JobDataSource model = MyUtils.model2Entity(params, JobDataSource.class);
            JobDataSource jobDataSource = MyUtils.copyProperties(model, entity);
            String jdbcUrl = jobDataSource.getJdbcUrl();
            if (StringUtils.isEmpty(jdbcUrl)) {
                throw new ValidException("数据源jdbc连接字符串不能为空");
            }
            if (!jdbcUrl.contains("/")) {
                throw new ValidException("数据源jdbc连接字符串格式不对");
            }

            jobDataSource.setDbName(jdbcUrl.substring(jdbcUrl.lastIndexOf('/') + 1));
            tokenService.updateUpdateIdAndTime(request, jobDataSource);
            jobDataSourceDao.save(jobDataSource);
            operationLogService.insert(new OperationLog("数据源管理—修改", "数据源管理", "修改了数据源名称为:" + params.getString("name"), "1"), currentWebAdminUserEntity.getId(), currentWebAdminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("数据源管理—修改", "数据源管理", "修改了数据源名称为:" + params.getString("name"), "0"), currentWebAdminUserEntity.getId(), currentWebAdminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 删除数据源数据
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        if (groupTaskDao.existsByDataSourceSourceIdOrDataSourceTargetId(id, id)) {
            throw new ValidException("该数据源已被配置到任务组中使用，请先在任务组管理中修改相应任务组！");
        }
        JobDataSource entity = jobDataSourceDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        try {
            jobDataSourceDao.delete(entity);
            operationLogService.insert(new OperationLog("数据源管理—删除", "数据源管理理", "删除数据源" + entity.getName(), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("数据源管理—删除", "数据源管理", "删除数据源" + entity.getName(), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 修改数据源状态  生效、失效
     */
    public void updateStatus(JSONObject params, HttpServletRequest request) throws Exception {
        String id = params.getString("id");
        JobDataSource entity = jobDataSourceDao.findById(id).orElseThrow(() -> new ValidException("找不到记录:" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            if ("启用".equals(params.getString("status"))) {
                entity.setStatus("生效");
            } else {
                entity.setStatus("失效");
            }
            tokenService.updateUpdateIdAndTime(request, entity);
            jobDataSourceDao.save(entity);
            operationLogService.insert(new OperationLog("数据源管理—修改", "数据源管理", "修改了数据源名称为:" + params.getString("name"), "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("数据源管理—修改", "数据源管理", "修改了数据源名称为:" + params.getString("name"), "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 测试数据源
     *
     * @param jobDatasource
     * @return
     * @throws IOException
     */
    public Boolean dataSourceTest(JobDataSource jobDatasource) throws IOException, ValidException {
        if (Constants.HBASE.equals(jobDatasource.getType())) {
//            return new HBaseQueryTool(jobDatasource).dataSourceTest();
        }
        String userName = jobDatasource.getUserName();
        //  判断账密是否为密文
        if (userName == null) {
            throw new ValidException("用户名不能为空");
        }
        String pwd = jobDatasource.getPassword();
        if (pwd == null) {
            throw new ValidException("密码不能为空");
        }
        if (Constants.MONGODB.equals(jobDatasource.getType())) {
//            return new MongoDBQueryTool(jobDatasource).dataSourceTest(jobDatasource.getDbName());
        }
        BaseQueryTool queryTool = QueryToolFactory.getByDbType(jobDatasource);
        return queryTool.dataSourceTest();
    }
}
