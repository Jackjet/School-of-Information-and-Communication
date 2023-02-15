package d1.project.caict.bigdata.goverance.service;

import d1.project.caict.bigdata.goverance.dao.OperationLogDao;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import org.springframework.stereotype.Service;

/**
 * @author lin
 */
@Service
public class OperationLogService {

    private final OperationLogDao operationLogDao;

    public OperationLogService(OperationLogDao operationLogDao) {
        this.operationLogDao = operationLogDao;
    }


    /**
     * 新增操作日志
     * 例1:insert(new OperationLog("用户管理—添加", "运维行为管理—用户管理", "添加了一个用户:" + name, "1"), request);
     */
    public void insert(OperationLog operationLog, String id, String name) throws Exception {
        operationLog.setId(id);
        operationLog.setOperand(name);
        operationLog.setSource("大数据治理平台");
        operationLogDao.save(operationLog);
    }


}
