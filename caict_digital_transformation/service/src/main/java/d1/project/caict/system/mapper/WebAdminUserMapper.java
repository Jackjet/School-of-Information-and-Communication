package d1.project.caict.system.mapper;

import d1.project.caict.common.utils.MapperFormatStrategy;
import d1.project.caict.system.entity.WebAdminUser;
import d1.project.caict.system.model.WebAdminUserExcelExport;
import d1.project.caict.system.model.WebAdminUserExcelImport;
import d1.project.caict.system.model.WebAdminUserVm;
import d1.project.caict.system.model.vm.WebAdminUserInsertVm;
import d1.project.caict.system.model.vm.WebAdminUserUpdateVm;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * d1project
 *
 * @author kikki
 * @date 2020-09-08 14:28
 */
@Mapper(uses = MapperFormatStrategy.class)
public interface WebAdminUserMapper {

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "roleName", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    WebAdminUser dtoFormatIntoInsertEntity(WebAdminUserInsertVm insertVm);

    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    WebAdminUser dtoFormatIntoInsertEntity(WebAdminUserExcelImport insertVm);


    @Mapping(target = "roleId", ignore = true)
    @Mapping(target = "organizationId", ignore = true)
    WebAdminUserVm entityFormatIntoDto(WebAdminUser insertVm);

    List<WebAdminUserVm> entityListFormatIntoDtoList(List<WebAdminUser> insertVm);


    List<WebAdminUserExcelExport> entityListFormatIntoExcelDtoList(List<WebAdminUser> insertVm);

    WebAdminUserExcelExport entityFormatIntoExcelDto(WebAdminUser insertVm);

    @Mapping(target = "roleName", ignore = true)
    @Mapping(target = "organizationName", ignore = true)
    @Mapping(target = "updateTime", ignore = true)
    @Mapping(target = "updateById", ignore = true)
    @Mapping(target = "pinyin", ignore = true)
    @Mapping(target = "password", ignore = true)
    @Mapping(target = "lastSignInTime", ignore = true)
    @Mapping(target = "enable", ignore = true)
    @Mapping(target = "createTime", ignore = true)
    @Mapping(target = "createById", ignore = true)
    @Mapping(target = "account", ignore = true)
    void copyProperties(WebAdminUserUpdateVm source, @MappingTarget WebAdminUser target);
}
