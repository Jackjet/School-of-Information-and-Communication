package d1.project.cms.dao.template;

import d1.project.cms.entity.template.DataField;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

/**
 * @author baozh
 */
public interface DataFieldDao extends JpaRepository<DataField,String>, JpaSpecificationExecutor<DataField> {

    /**
     * 根据模板ID和字段查询
     * @param templateId 模板ID
     * @return 查询结果
     */
    boolean existsByTemplateId(String templateId);

    /**
     * 根据模板ID和字段查询
     * @param templateId 模板ID
     * @param field 字段
     * @return 查询结果
     */
    boolean existsAllByTemplateIdAndField(String templateId,String field);

    /**
     * 根据模板ID和字段查询
     * @param templateId 模板ID
     * @param field 字段
     * @param type 类型
     * @return 查询结果
     */
    boolean existsAllByTemplateIdAndFieldAndType(String templateId,String field,String type);

    /**
     * 根据模板ID和字段查询
     * @param templateId 模板ID
     * @param field 字段
     * @param typeId 字段
     * @param id ID
     * @return 查询结果
     */
    boolean existsAllByTemplateIdAndFieldAndTypeAndIdNot(String templateId,String field,String typeId,String id);

    /**
     * 根据模板ID删除
     * @param templateId 模板ID
     */
    @Modifying
    @Transactional(rollbackFor = Exception.class)
    void deleteAllByTemplateId(String templateId);

    List<DataField> findAllByTemplateId(String templateId);

    List<DataField> findAllByTemplateIdAndType(String templateId,String type);
}
