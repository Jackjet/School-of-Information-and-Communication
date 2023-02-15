package d1.project.caict.bigdata.goverance.service.task;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.task.GroupTaskLogDao;
import d1.project.caict.bigdata.goverance.entity.task.GroupTaskLog;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import d1.project.caict.bigdata.goverance.utils.MyUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.Optional;

/**
 * @author maoyy
 */

@Service
public class GroupTaskLogService {
    private final GroupTaskLogDao groupTaskLogDao;
    private final OperationLogService operationLogService;
    final
    WebAdminService webAdminService;

    @Autowired
    public GroupTaskLogService(GroupTaskLogDao groupTaskLogDao, OperationLogService operationLogService, WebAdminService webAdminService) {
        this.groupTaskLogDao = groupTaskLogDao;
        this.operationLogService = operationLogService;
        this.webAdminService = webAdminService;
    }

    /**
     * 查询任务组日志信息
     *
     * @param params
     * @return
     * @throws Exception
     */
    public Page<GroupTaskLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<GroupTaskLog> builder = new SpecificationBuilder<>();
        Specification<GroupTaskLog> specification = builder.init(params)
                .sContain("groupTaskName", "groupTaskName")
                .sEqual("sourceCatalog", "sourceCatalog")
                .sEqual("dataSourceSourceId", "dataSourceSourceId")
                .sEqual("targetCatalog", "targetCatalog")
                .sEqual("dataSourceTargetId", "dataSourceTargetId")
                .sEqual("status", "status")
                .dOrder("startTime").build();
        Pageable pageable = MyUtils.getPageable(params.getInteger("page"), params.getInteger("size"), Sort.by(Sort.Direction.DESC, "startTime", "createTime"));
        return this.groupTaskLogDao.findAll(specification, pageable);
    }

    public GroupTaskLog insert(JSONObject params, String currentWebadminUserId, String currentWebadminUserName) throws Exception {
//        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        GroupTaskLog dataQuality = MyUtils.model2Entity(params, GroupTaskLog.class);
        String name = dataQuality.getGroupTaskName();
        try {
            GroupTaskLog app1 = MyUtils.initInsert(dataQuality, currentWebadminUserId);
            app1.setStartTime(new Date());
            app1.setStatus("执行中");
            GroupTaskLog save = this.groupTaskLogDao.save(app1);
            operationLogService.insert(new OperationLog("任务组管理—任务组手动执行", "接入任务管理-任务组管理", "手动执行任务组:" + name, "1"), currentWebadminUserId, currentWebadminUserName);
            return save;
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("任务组管理—任务组手动执行", "接入任务管理-任务组管理", "手动执行任务组:" + name, "0"), currentWebadminUserId, currentWebadminUserName);
            throw new ValidException(e.getMessage());
        }
    }

    /**
     * 查询任务组日志详细信息
     *
     * @param id
     * @return
     */
    public Optional<GroupTaskLog> findById(String id) {
        return groupTaskLogDao.findById(id);
    }

    public void update(JSONObject params) throws Exception {
        GroupTaskLog model = MyUtils.model2Entity(params, GroupTaskLog.class);
        GroupTaskLog groupTaskLog = groupTaskLogDao.findById(model.getId()).orElseThrow(() -> new ValidException("找不到任务组日志记录:" + model.getId()));
        GroupTaskLog app1 = MyUtils.initUpdate(model, null);
        GroupTaskLog app2 = MyUtils.copyProperties(app1, groupTaskLog);

        groupTaskLogDao.save(app2);
    }
}
