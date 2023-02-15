package d1.project.caict.bigdata.goverance.service;

import com.alibaba.fastjson.JSONObject;
import d1.framework.permission.utils.MyUtils;
import d1.framework.webapi.configuration.ValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.caict.bigdata.goverance.dao.TemplateDao;
import d1.project.caict.bigdata.goverance.entity.Template;
import d1.project.caict.bigdata.goverance.entity.WebAdminUser;
import d1.project.caict.bigdata.goverance.entity.OperationLog;
import jdk.nashorn.internal.runtime.regexp.joni.exception.ValueException;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class TemplateService {

    private final
    TemplateDao dao;

    private final
    WebAdminService webAdminService;

    private final
    OperationLogService operationLogService;

    public TemplateService(TemplateDao dao, WebAdminService webAdminService, OperationLogService operationLogService) {
        this.dao = dao;
        this.webAdminService = webAdminService;
        this.operationLogService = operationLogService;
    }


    /**
     * 添加
     *
     * @param model   参数
     * @param request 客户端请求
     * @author Kikki  2020/7/5 12:10
     */
    public void insert(JSONObject model, HttpServletRequest request) throws Exception {
        String name = model.getString("name");
        if (dao.existsByName(name)) {
            throw new Exception("模板名称已存在");
        }
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            Template dataQuality = MyUtils.model2Entity(model, Template.class);

            dataQuality.setCreateByName(currentWebadminUserEntity.getName());
            Template app1 = MyUtils.initInsert(dataQuality, currentWebadminUserEntity.getId());
            app1.setUpdateByName("");
            app1.setReferencesCount(0);
            dao.save(app1);
            operationLogService.insert(new OperationLog("模板管理—添加", "数据模板—模板管理", "添加了模板:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("模板管理—添加", "数据模板—模板管理", "添加了模板:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    /**
     * 删除
     *
     * @param id id
     * @author Kikki  2020/7/5 12:10
     */
    public void deleteById(String id, HttpServletRequest request) throws Exception {
        Template entityOptional = dao.findById(id).orElseThrow(() -> new ValueException("找不到记录：" + id));
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        String name = entityOptional.getName();

        try {
            dao.deleteById(id);
            operationLogService.insert(new OperationLog("模板管理—删除", "数据模板—模板管理", "删除了模板:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("模板管理—删除", "数据模板—模板管理", "删除了模板:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }

    }

    /**
     * 修改
     *
     * @param jsonObject 参数
     * @param request    客户端请求
     * @author Kikki  2020/7/5 12:10
     */
    public void update(JSONObject jsonObject, HttpServletRequest request) throws Exception {
        Template model = MyUtils.model2Entity(jsonObject, Template.class);
        String id = model.getId();
        Template entityOptional = dao.findById(id).orElseThrow(() -> new ValueException("找不到记录：" + id));
        String name = entityOptional.getName();
        WebAdminUser currentWebadminUserEntity = webAdminService.getCurrentWebadminUserEntity(request);
        try {
            /* 更新前需验证 */
            if (StringUtils.isEmpty(id)) {
                throw new ValidException("id不能为空");
            }

            model.setUpdateByName(currentWebadminUserEntity.getName());
            /* 保存信息 */
            Template app1 = MyUtils.initUpdate(model, currentWebadminUserEntity.getId());
            Template app2 = MyUtils.copyProperties(app1, entityOptional);
            dao.save(app2);
            operationLogService.insert(new OperationLog("模板管理—修改", "数据模板—模板管理", "修改了模板:" + name, "1"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
        } catch (Exception e) {
            operationLogService.insert(new OperationLog("模板管理—修改", "数据模板—模板管理", "修改了模板:" + name, "0"), currentWebadminUserEntity.getId(), currentWebadminUserEntity.getName());
            throw new Exception(e.getMessage());
        }
    }

    public void addReferencesCount(String id) throws Exception {
        Template template = findById(id).orElseThrow(() -> new Exception("找不到记录：" + id));
        template.setReferencesCount(template.getReferencesCount() + 1);
        dao.save(template);
    }

    public void lessReferencesCount(String id) throws Exception {
        Template template = findById(id).orElseThrow(() -> new Exception("找不到记录：" + id));
        template.setReferencesCount(template.getReferencesCount() - 1);
        dao.save(template);
    }

    public Page<Template> findAll(JSONObject params) throws Exception {
        SpecificationBuilder<Template> builder = new SpecificationBuilder<>();
        Specification<Template> specification = builder.init(params)
                .sContain("name", "name")
                .dOrder("createTime")
                .build();
        return dao.findAll(specification, builder.getPageable());
    }

    public Optional<Template> findById(String id) {
        return dao.findById(id);
    }

    public Map<String, Object> countGroupConstraintTypeByCreateTime(Date createTime, Date createTime2) {
        Calendar cal1 = Calendar.getInstance();
        cal1.setTime(createTime);
        Calendar cal2 = Calendar.getInstance();
        cal2.setTime(createTime2);
        List<Map<String, Object>> maps = dao.countGroupConstraintTypeByCreateTime(cal1, cal2);
        Map<String, Object> stringLongMap = new HashMap<>(11);
        stringLongMap.put("完整性非空约束", 0L);
        stringLongMap.put("实体唯一性约束", 0L);
        stringLongMap.put("有效性长度约束", 0L);
        stringLongMap.put("有效性标志取值约束", 0L);
        stringLongMap.put("有效性代码值域约束", 0L);
        stringLongMap.put("有效性内容规范约束", 0L);
        stringLongMap.put("存在一致性依赖约束", 0L);
        stringLongMap.put("等值一致性依赖约束", 0L);
        stringLongMap.put("逻辑一致性依赖约束", 0L);
        stringLongMap.put("取值准确性约束", 0L);
        stringLongMap.put("时效性约束", 0L);
        maps.forEach(stringObjectMap -> {
            stringLongMap.replace(stringObjectMap.get("name").toString(), stringObjectMap.get("value"));
        });
        List<String> strings = new ArrayList<>();
        List<Object> longs = new ArrayList<>();
        stringLongMap.forEach((s, o) -> {
            strings.add(s);
            longs.add(o);
        });
        Map<String, Object> objectMap = new HashMap<>();
        objectMap.put("dataQualityType", strings);
        objectMap.put("count", longs);
        return objectMap;
    }

    public List<JSONObject> dataQualityIndex() throws ParseException {
        Map<String, Object> stringLongMap = new HashMap<>(11);
        JSONObject objectMap = new JSONObject();
        stringLongMap.put("完整性非空约束", objectMap);
        stringLongMap.put("实体唯一性约束", objectMap);
        stringLongMap.put("有效性长度约束", objectMap);
        stringLongMap.put("有效性标志取值约束", objectMap);
        stringLongMap.put("有效性代码值域约束", objectMap);
        stringLongMap.put("有效性内容规范约束", objectMap);
        stringLongMap.put("存在一致性依赖约束", objectMap);
        stringLongMap.put("等值一致性依赖约束", objectMap);
        stringLongMap.put("逻辑一致性依赖约束", objectMap);
        stringLongMap.put("取值准确性约束", objectMap);
        stringLongMap.put("时效性约束", objectMap);
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String format = simpleDateFormat.format(new Date());
//        String start = format + " 00:00:00";
//        String end = format + " 23:59:59";
//        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//        Date parse = dateFormat.parse(start);
//        Date parse1 = dateFormat.parse(end);

//        Calendar cal1 = Calendar.getInstance();
//        cal1.setTime(parse);
//        Calendar cal2 = Calendar.getInstance();
//        cal2.setTime(parse1);

        List<Map<String, Object>> maps = dao.countGroupConstraintTypeByRuleLogCreateTime();
        maps.forEach(stringObjectMap -> {
            Object name = stringObjectMap.get("name");
            Long checkCount = (Long) stringObjectMap.get("value1");
            Long errorCount = (Long) stringObjectMap.get("value2");
            double value = Double.parseDouble(String.format("%.2f", ((errorCount.doubleValue() / checkCount.doubleValue()) * 100)));
            value = Double.isNaN(value) ? 0 : value;
            JSONObject objectMapObject = new JSONObject();
            objectMapObject.put("count", checkCount);
            objectMapObject.put("errorCount", errorCount);
            objectMapObject.put("accuracy", checkCount == 0 ? 0 : 100.00 - value);
            stringLongMap.replace(name.toString(), objectMapObject);
        });


        List<JSONObject> maps1 = new ArrayList<>();
        stringLongMap.forEach((s, o) -> {
            JSONObject obj = (JSONObject) o;
            Object countObj = obj.get("count");
            Object errorCountObj = obj.get("errorCount");
            Object accuracyObj = obj.get("accuracy");

            JSONObject jsonObject = new JSONObject();
            jsonObject.put("name", s);
            jsonObject.put("count", countObj == null ? 0 : countObj);
            jsonObject.put("errorCount", errorCountObj == null ? 0 : errorCountObj);
            jsonObject.put("accuracy", accuracyObj == null ? 0 : accuracyObj);
            jsonObject.put("date", format);

            maps1.add(jsonObject);

        });
        return maps1;
    }

    public boolean existsByDataQualityType(String dataQualityType) {
        return dao.existsByDataQualityType(dataQualityType);
    }
}
