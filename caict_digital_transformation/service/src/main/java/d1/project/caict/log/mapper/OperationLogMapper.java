package d1.project.caict.log.mapper;

import d1.project.caict.log.entity.OperationLog;
import d1.project.caict.log.model.OperationLogExcel;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:28
 */
@Mapper
public interface OperationLogMapper {


    @Mapping(target = "type", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createByPhone", ignore = true)
    @Mapping(target = "createByName", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "createByAccount", ignore = true)
    OperationLog dtoFormatIntoRequestInsertEntity(d1.project.caict.common.model.OperationLog insertVm);

    List<OperationLogExcel> entityListFormatIntoExcelDtoList(List<OperationLog> insertVm);
}
