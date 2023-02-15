package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.ExaminationGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:57
 */
public interface ExaminationGroupDao extends JpaRepository<ExaminationGroup, String>, JpaSpecificationExecutor<ExaminationGroup> {

    /**
     * 查询非当前ID下该名称是否存在
     * @param name 分组名称
     * @param id id
     * @return 是否存在
     */
    boolean existsAllByNameAndIdNot(String name ,String id);

    /**
     * 查询非当前ID下该名称是否存在
     * @param name 分组名称
     * @return 是否存在
     */
    boolean existsAllByName(String name);
}
