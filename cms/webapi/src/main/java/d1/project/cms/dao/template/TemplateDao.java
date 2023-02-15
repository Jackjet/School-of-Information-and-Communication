package d1.project.cms.dao.template;

import d1.project.cms.entity.template.Template;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author baozh
 */
public interface TemplateDao extends JpaRepository<Template,String>, JpaSpecificationExecutor<Template> {

    /**
     * 根据名字查询是否存在
     * @param name 名字
     * @return 查询结果
     */
    boolean existsAllByName(String name);

    /**
     * 根据名字查询是否存在
     * @param name 名字
     * @param id ID
     * @return 查询结果
     */
    boolean existsAllByNameAndIdNot(String name,String id);

    @Query(value = "select id from d1_template where is_pc_list = ?1 or is_app_list = ?2" ,nativeQuery = true)
    List<String> findAllByIsPcListOrIsAppList(String isPcList,String isAppList);
}
