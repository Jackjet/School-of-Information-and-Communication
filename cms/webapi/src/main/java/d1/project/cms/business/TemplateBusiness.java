package d1.project.cms.business;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.cms.Constants;
import d1.project.cms.entity.template.DataField;
import d1.project.cms.entity.template.Template;
import d1.project.cms.model.template.DataFieldInsertOrUpdateVm;
import d1.project.cms.model.template.TemplateInsertOrUpdateVm;
import d1.project.cms.service.WebAdminService;
import d1.project.cms.service.column.ColumnService;
import d1.project.cms.service.template.DataFieldService;
import d1.project.cms.service.template.TemplateService;
import d1.project.cms.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.Calendar;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class TemplateBusiness {

    private final TemplateService templateService;
    private final DataFieldService dataFieldService;
    private final WebAdminService webAdminService;
    private final ColumnService columnService;

    public TemplateBusiness(TemplateService templateService, DataFieldService dataFieldService, WebAdminService webAdminService, ColumnService columnService) {
        this.templateService = templateService;
        this.dataFieldService = dataFieldService;
        this.webAdminService = webAdminService;
        this.columnService = columnService;
    }

    public Page<Template> findTemplate(JSONObject params) throws Exception{
        return templateService.find(params);
    }

    public Template findTemplateById(String id) throws Exception{
        return templateService.findById(id);
    }

    /**
     * 添加模板信息
     * @param vm 添加信息
     * @param request 请求信息
     * @throws Exception 向上抛出异常
     */
    public void insertTemplate(TemplateInsertOrUpdateVm vm, HttpServletRequest request) throws Exception{
        String userId = webAdminService.getUserId(request);
        Template template = new Template();
        BeanUtils.copyProperties(vm,template);
        String id = MyUtils.generate32Id();
        template.setId(id);
        template.setCreateById(userId);
        template.setCreateTime(Calendar.getInstance());
        templateService.insert(template);
        List<DataFieldInsertOrUpdateVm> list = vm.getDataList();
        if (list.size()>0){
            for (DataFieldInsertOrUpdateVm dataFieldInsertOrUpdateVm : list) {
                DataField dataField = new DataField();
                BeanUtils.copyProperties(dataFieldInsertOrUpdateVm,dataField);
                dataField.setTemplateId(id);
                dataField.setCreateById(userId);
                dataField.setCreateTime(Calendar.getInstance());
                dataField.setId(MyUtils.generate32Id());
                dataFieldService.insert(dataField);
            }
        }
    }

    /**
     * 编辑模板
     * @param vm 编辑信息
     * @param request 请求参数
     * @throws Exception 向上抛出异常
     */
    public void updateTemplate(TemplateInsertOrUpdateVm vm, HttpServletRequest request) throws Exception{
        String userId = webAdminService.getUserId(request);
        Template template = templateService.findById(vm.getId());
        BeanUtils.copyProperties(vm,template);
        template.setUpdateById(userId);
        template.setUpdateTime(Calendar.getInstance());
        templateService.update(template);
    }

    /**
     * 根据ID删除模板
     * @param id ID
     */
    public void deleteTemplate(String id) throws Exception{
        if (columnService.existsByTemplateId(id)){
            throw new DoValidException(Constants.TEMPLATE_IN_USED);
        }
        templateService.delete(id);
        dataFieldService.deleteByTemplateId(id);
    }

    /**
     * 查询数据字段
     * @param params 数据字段信息
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public Page<DataField> findData(JSONObject params) throws Exception{
        return dataFieldService.find(params);
    }

    /**
     * 查询数据字段详情
     * @param id 数据ID
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public DataField findDataById(String id) throws Exception{
        return dataFieldService.findById(id);
    }

    /**
     * 添加数据字段信息
     * @param vm 添加信息
     * @param request 请求信息
     * @throws Exception 向上抛出异常
     */
    public void insertData(DataFieldInsertOrUpdateVm vm,HttpServletRequest request) throws Exception{
        String userId = webAdminService.getUserId(request);
        DataField dataField = new DataField();
        BeanUtils.copyProperties(vm,dataField);
        dataField.setId(MyUtils.generate32Id());
        dataField.setCreateById(userId);
        dataField.setCreateTime(Calendar.getInstance());
        dataFieldService.insert(dataField);
    }

    /**
     * 数据字段编辑
     * @param vm 编辑信息
     * @param request 请求信息
     * @throws Exception 向上抛出异常
     */
    public void updateData(DataFieldInsertOrUpdateVm vm,HttpServletRequest request) throws Exception{
        String userId = webAdminService.getUserId(request);
        DataField dataField = dataFieldService.findById(vm.getId());
        BeanUtils.copyProperties(vm,dataField);
        dataField.setUpdateById(userId);
        dataField.setUpdateTime(Calendar.getInstance());
        dataFieldService.update(dataField);
    }

    /**
     * 根据ID删除模板
     * @param id ID
     */
    public void deleteData(String id){
        dataFieldService.delete(id);
    }

    public void uploadJson(MultipartFile multipartFile, String templateId,HttpServletRequest request) throws Exception{
        Reader reader = new InputStreamReader(multipartFile.getInputStream(), StandardCharsets.UTF_8);
        BufferedReader br = new BufferedReader(reader);
        StringBuilder stb = new StringBuilder();
        String line;
        while ((line = br.readLine()) != null) {
            if (!StringUtils.isEmpty(line)){
                stb.append(line);
            }
        }
        br.close();
        reader.close();
        String userId = webAdminService.getUserId(request);
        String jsonStr = stb.toString();
        JSONArray jsonArray = JSONArray.parseArray(jsonStr);
        List<DataField> dataList = jsonArray.toJavaList(DataField.class);
        for (DataField dataField : dataList) {
            dataField.setId(MyUtils.generate32Id());
            dataField.setTemplateId(templateId);
            dataField.setCreateById(userId);
            dataField.setCreateTime(Calendar.getInstance());
            dataFieldService.insert(dataField);
        }
    }
}
