package d1.project.caict.bigdata.goverance.service.rule;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.rule.RuleUpdateLogDao;
import d1.project.caict.bigdata.goverance.entity.rule.RuleUpdateLog;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.function.Supplier;

@Service
public class RuleUpdateLogService {

    final
    RuleUpdateLogDao dao;

    public RuleUpdateLogService(RuleUpdateLogDao dao) {
        this.dao = dao;
    }

    public void insert(Supplier<RuleUpdateLog> model) throws Exception {
        insert(model.get());
    }

    public void insert(RuleUpdateLog model) throws Exception {
        dao.save(model);
    }

    public Page<RuleUpdateLog> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<RuleUpdateLog> builder = new SpecificationBuilder<>();
        Specification<RuleUpdateLog> specification = builder.init(params)
                .sEqual("ruleId","ruleId")
                .dOrder("updateTime")
                .build();
        return dao.findAll(specification, builder.getPageable());
    }

    public Optional<RuleUpdateLog> findById(String id) {
        return dao.findById(id);
    }

}
