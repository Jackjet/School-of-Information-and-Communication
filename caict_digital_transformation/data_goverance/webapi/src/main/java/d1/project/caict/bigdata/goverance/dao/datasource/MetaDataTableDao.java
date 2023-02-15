package d1.project.caict.bigdata.goverance.dao.datasource;

import d1.project.caict.bigdata.goverance.entity.datasource.MetaDataTable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

/**
 * @author maoyy
 */

public interface MetaDataTableDao extends JpaRepository<MetaDataTable, String>, JpaSpecificationExecutor<MetaDataTable> {

    MetaDataTable findByDataSourceIdAndTableName(String dataSourceId, String tableName);

    @Query(value = "select table_name from d1_meta_data_table where data_source_id =(?1)", nativeQuery = true)
    List<String> findTableNameByDataSourceId(String dataSourceId);

    @Modifying
    @Query(value = "delete from d1_meta_data_table where data_source_id =?1 and table_name in ?2", nativeQuery = true)
    void deleteByDataSourceIdAndTableName(String dataSourceId, List<String> delTables);

    @Query(value = "select id from d1_meta_data_table where data_source_id =(?1) and table_name =(?2)", nativeQuery = true)
    List<String> findFirstIdWithDataSourceIdAndTableName(String dataSourceId, String tableName);
}
