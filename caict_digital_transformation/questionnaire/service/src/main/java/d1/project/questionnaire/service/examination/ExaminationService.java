package d1.project.questionnaire.service.examination;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.ExaminationDao;
import d1.project.questionnaire.entity.examination.Examination;
import d1.project.questionnaire.model.exam.examination.ExaminationFindAllVm;
import d1.project.questionnaire.model.exam.examination.ExaminationInsert;
import d1.project.questionnaire.util.BaseUtils;
import d1.project.questionnaire.util.Constants;
import d1.project.questionnaire.util.SpecificationBuilder;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;
import java.util.function.Function;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:44
 */

@Service
public class ExaminationService extends DoServiceImpBase<Examination> {
    final
    ExaminationDao examinationDao;

    public ExaminationService(ExaminationDao examinationDao) {
        this.examinationDao = examinationDao;
    }

    public Examination insert(ExaminationInsert insertVm, HttpServletRequest request) throws Exception {
        String userId = getCurrentUser(request).getString("id");
        Examination audio = BaseUtils.copyPropertiesInsert(insertVm, userId, Examination.class);
        insert(audio);
        return audio;
    }

    public Page<Examination> findAll(ExaminationFindAllVm findVm) throws Exception {
        SpecificationBuilder<Examination> builder = new SpecificationBuilder<>();
        Specification<Examination> build = builder.init((JSONObject) JSON.toJSON(findVm))
                .sContain("name", "name")
                .dOrder("createTime").build();
        return examinationDao.findAll(build, builder.getPageable());
    }

    public Examination findByInfoId(String id) throws Exception{
        Examination exam = findById(id);
        if (exam == null) {
            throw new Exception(Constants.DATA_IS_NULL_OR_FAIL);
        }
        return exam;
    }
    public Long getLong(Function<ExaminationDao, Long> daoLongFunction) {
        return daoLongFunction.apply(examinationDao);
    }
    public Optional<Examination> getOptional(Function<ExaminationDao, Optional<Examination>> daoLongFunction) {
        return daoLongFunction.apply(examinationDao);
    }

    public boolean existsAllByName(String name){
        return examinationDao.existsAllByName(name);
    }

    public boolean existsAllByNameAndIdNot(String name,String id){
        return examinationDao.existsAllByNameAndIdNot(name,id);
    }

    @Override
    protected JpaRepository<Examination, String> getDao() {
        return examinationDao;
    }
}
