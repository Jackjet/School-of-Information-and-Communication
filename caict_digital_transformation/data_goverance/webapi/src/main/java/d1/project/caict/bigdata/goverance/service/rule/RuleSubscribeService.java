package d1.project.caict.bigdata.goverance.service.rule;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.rule.RuleSubscribeDao;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.rule.RuleSubscribe;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import d1.project.caict.bigdata.goverance.service.WebAdminService;
import d1.project.caict.bigdata.goverance.service.OperationLogService;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Calendar;
import java.util.Optional;

@Service
public class RuleSubscribeService {
    private final
    RuleSubscribeDao dao;

    private final
    OperationLogService operationLogService;

    private final
    WebAdminService webAdminService;

    public RuleSubscribeService(RuleSubscribeDao dao, OperationLogService operationLogService, WebAdminService webAdminService) {
        this.dao = dao;
        this.operationLogService = operationLogService;
        this.webAdminService = webAdminService;
    }


    /**
     * 添加
     *
     * @param model   参数
     * @param request 客户端请求
     * @author Kikki  2020/7/5 12:10
     */
    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String name = model.getString("ruleName");
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            RuleSubscribe dataQuality = MyUtils.model2Entity(model, RuleSubscribe.class);

            dataQuality.setItcode(currentWebadminUserEntity.getId());
            dataQuality.setCreateTime(Calendar.getInstance());
            dataQuality.setId(MyUtils.generate32Id());
            if (dao.existsByItcodeAndRuleId(dataQuality.getItcode(), dataQuality.getRuleId())) {
                throw new Exception("重复订阅");
            }
            dao.save(dataQuality);
            operationLogService.insert(new OperationLog("规则管理—添加", "规则管理—规则管理", "添加了规则:" + name, "1"),  currentWebadminUserEntity.getId(),currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—添加", "规则管理—规则管理", "添加了规则:" + name, "0"),  currentWebadminUserEntity.getId(),currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 取消订阅
     *
     * @param id id
     * @author Kikki  2020/7/5 12:10
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        RuleSubscribe entityOptional = dao.findById(id).orElseThrow(() -> new ValueException("找不到记录：" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String name = entityOptional.getRuleName();
        try {
            dao.deleteByIdAndItcode(id, currentWebadminUserEntity.getId());
            operationLogService.insert(new OperationLog("规则管理—取消订阅", "规则管理—规则管理", "取消订阅了规则:" + name, "1"),  currentWebadminUserEntity.getId(),currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("规则管理—取消订阅", "规则管理—规则管理", "取消订阅了规则:" + name, "0"),  currentWebadminUserEntity.getId(),currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    public Optional<RuleSubscribe> findByItcode(String itcode) {
        return dao.findByItcode(itcode);
    }

    public Page<RuleSubscribe> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<RuleSubscribe> builder = new SpecificationBuilder<>();
        Specification<RuleSubscribe> specification = builder.init(params)
                .sEqual("itcode", "itcode")
                .dOrder("createTime")
                .build();
        return dao.findAll(specification, builder.getPageable());
    }

    public Optional<RuleSubscribe> findByItcodeAndRuleId(String ruleId, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        return dao.findByItcodeAndRuleId(currentWebadminUserEntity.getId(), ruleId);
    }

    public Page<RuleSubscribe> findByCurrentUser(JSONObject model, HttpServletRequest request) throws Exception {
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        model.put("itcode", currentWebadminUserEntity.getId());
        return findAll(model);
    }

}
