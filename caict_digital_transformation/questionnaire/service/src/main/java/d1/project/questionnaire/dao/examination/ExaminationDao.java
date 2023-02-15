package d1.project.questionnaire.dao.examination;

import d1.project.questionnaire.entity.examination.Examination;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

/**
 * liangxin_new
 *
 * @author kikki
 * @date 2020-10-12 14:44
 */

public interface ExaminationDao extends JpaRepository<Examination, String>, JpaSpecificationExecutor<Examination> {

    /**
     * 查询考试名是否存在
     * @param name 用户名
     * @return 查询结果
     */
    boolean existsAllByName(String name);

    /**
     * 查询考试名是否存在
     * @param name 用户名
     * @param id 用户名
     * @return 查询结果
     */
    boolean existsAllByNameAndIdNot(String name,String id);
}
