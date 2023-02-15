package d1.project.caict.bigdata.goverance.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.GroupWebAdminUserDao;
import d1.project.caict.bigdata.goverance.entity.GroupWebAdminUser;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Optional;

@Service
public class GroupWebAdminUserService {

    final
    GroupWebAdminUserDao dao;

    private final
    WebAdminService webAdminService;

    private final
    OperationLogService operationLogService;

    public GroupWebAdminUserService(GroupWebAdminUserDao groupWebAdminUserDao, WebAdminService webAdminService, OperationLogService operationLogService) {
        this.dao = groupWebAdminUserDao;
        this.webAdminService = webAdminService;
        this.operationLogService = operationLogService;
    }


    /**
     * 添加
     *
     * @param model   参数
     * @param request 客户端请求
     * @author Kikki  2020/7/5 10:55
     */
    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String name = model.getString("name");
        if (dao.existsByName(name)) {
            throw new Exception("用户组管理名称已存在");
        }
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            GroupWebAdminUser dataQuality = MyUtils.model2Entity(model, GroupWebAdminUser.class);

            dataQuality.setCreateByName(currentWebadminUserEntity.getName());
            GroupWebAdminUser app1 = MyUtils.initInsert(dataQuality, currentWebadminUserEntity.getId());
            app1.setUpdateByName("");
            dao.save(app1);
            operationLogService.insert(new OperationLog("用户组管理—添加", "用户管理—用户组管理", "添加了分类:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户组管理—添加", "用户管理—用户组管理", "添加了分类:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id id
     * @author Kikki  2020/7/5 10:56
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        GroupWebAdminUser entityOptional = dao.findById(id).orElseThrow(() -> new ValueException("找不到记录：" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String name = entityOptional.getName();
        if (webAdminService.existsByGroupWebAdminUseName(name)) {
            throw new Exception("该用户组下存在用户请勿删除");
        }
        try {
            dao.deleteById(id);
            operationLogService.insert(new OperationLog("用户组管理—删除", "用户管理—用户组管理", "删除了分类:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户组管理—删除", "用户管理—用户组管理", "删除了分类:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 修改
     *
     * @param jsonObject 参数
     * @param request    客户端请求
     * @author Kikki  2020/7/5 11:07
     */
    public void update(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        GroupWebAdminUser model = MyUtils.model2Entity(jsonObject, GroupWebAdminUser.class);
        String id = model.getId();
        GroupWebAdminUser entityOptional = dao.findById(id).orElseThrow(() -> new ValueException("找不到记录：" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String name = entityOptional.getName();
        try {
            /* 更新前需验证 */
            if (StringUtils.isEmpty(id)) {
                throw new ValidException("id不能为空");
            }

            model.setUpdateByName(currentWebadminUserEntity.getName());
            /* 保存信息 */
            GroupWebAdminUser app1 = MyUtils.initUpdate(model, currentWebadminUserEntity.getId());
            GroupWebAdminUser app2 = MyUtils.copyProperties(app1, entityOptional);
            dao.save(app2);
            operationLogService.insert(new OperationLog("用户组管理—修改", "用户管理—用户组管理", "修改了分类:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("用户组管理—修改", "用户管理—用户组管理", "修改了分类:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }


    public Page<GroupWebAdminUser> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<GroupWebAdminUser> builder = new SpecificationBuilder<>();
        Specification<GroupWebAdminUser> specification = builder.init(params)
                .sEqual("name", "name")
                .dOrder("createTime")
                .build();
        return dao.findAll(specification, builder.getPageable());
    }

    public Optional<GroupWebAdminUser> findById(String id) {
        return dao.findById(id);
    }

}
