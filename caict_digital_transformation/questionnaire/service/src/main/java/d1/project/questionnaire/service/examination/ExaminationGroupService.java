package d1.project.questionnaire.service.examination;

import d1.framework.webapi.service.impl.DoServiceImpBase;
import d1.project.questionnaire.dao.examination.ExaminationGroupDao;
import d1.project.questionnaire.entity.examination.ExaminationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:57
 */
@Service
public class ExaminationGroupService extends DoServiceImpBase<ExaminationGroup> {
    private final ExaminationGroupDao examinationGroupDao;

    public ExaminationGroupService(ExaminationGroupDao examinationGroupDao) {
        this.examinationGroupDao = examinationGroupDao;
    }

    @Override
    protected JpaRepository<ExaminationGroup, String> getDao() {
        return examinationGroupDao;
    }

    /**
     * 添加题库分组
     *
     * @param name    题库名
     * @param request 请求信息
     * @throws Exception 添加异常向上抛出
     */
    public void insertExamGroup(String name, HttpServletRequest request) throws Exception {
        String userId = getCurrentUser(request).getString("id");
        ExaminationGroup examinationGroup = new ExaminationGroup();
        examinationGroup.setName(name);
        examinationGroup.setCreateById(userId);
        examinationGroup.setCreateTime(new Date());
        insert(examinationGroup);
    }

    /**
     * 添加题库分组
     *
     * @param name 题库名
     * @throws Exception 添加异常向上抛出
     */
    public void updateExamGroup(String name, String id) throws Exception {
        ExaminationGroup examinationGroup = new ExaminationGroup();
        examinationGroup.setName(name);
        examinationGroup.setId(id);
        update(examinationGroup);
    }

    public boolean existsAllByNameAndIdNot(String name, String id) {
        return examinationGroupDao.existsAllByNameAndIdNot(name, id);
    }

    public boolean existsAllByName(String name) {
        return examinationGroupDao.existsAllByName(name);
    }

}
