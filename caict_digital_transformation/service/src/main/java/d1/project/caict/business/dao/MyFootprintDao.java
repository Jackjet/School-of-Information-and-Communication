package d1.project.caict.business.dao;

import d1.project.caict.business.entity.MyFootprint;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author: maorui
 * @date: 2020/9/23
 */
public interface MyFootprintDao extends JpaRepository<MyFootprint, String>, JpaSpecificationExecutor<MyFootprint> {

    /**
     * 查询我的足迹列表
     */
    <T> Page<T> execSqlWithPage(@NotNull String sql, @NotNull Pageable pageable, @NotNull Class<T> clazz);

    /**
     * 根据日期查询我的足迹列表
     */
    List<MyFootprint> findAllByUserIdAndDayOrderByCreateTimeDesc(String userId, String day);

}
