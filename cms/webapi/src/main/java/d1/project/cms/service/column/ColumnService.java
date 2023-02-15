package d1.project.cms.service.column;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.project.cms.dao.column.ColumnDao;
import d1.project.cms.entity.column.Column;
import d1.project.cms.model.column.ColumnResultVm;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;
import java.util.Vector;

/**
 * @author libin
 */
@Service
public class ColumnService {
    private final ColumnDao columnDao;

    public ColumnService(ColumnDao columnDao) {
        this.columnDao = columnDao;
    }

    public Column findById(String id) throws DoValidException {
        return this.columnDao.findById(id).orElseThrow(() ->new DoValidException("栏目不存在"));
    }

    public void save(Column column){
        this.columnDao.save(column);
    }

    public void delete(Column column){
        this.columnDao.delete(column);
    }

    /**
     * 查询栏目列表
     *
     * @return 查询结果
     */
    public List<Column> findAll(){
        //最高层级栏目父id默认为 0
        return this.columnDao.findAll();
    }

    public List<Column> findAllByTemplateIdIn(List<String> templateIds){
        return this.columnDao.findAllByTemplateIdIn(templateIds);
    }

    /**
     * 查询栏目列表
     *
     * @param params 查询条件
     * @return 查询结果
     */
    public List<ColumnResultVm> findAll(JSONObject params) throws DoValidException {
        //最高层级栏目父id默认为 0
        List<Column> columns = this.columnDao.findAllByParentIdOrderByPower("0");

        List<ColumnResultVm> result = new Vector<>();

        //查询数据
        for (Column column : columns) {
            ColumnResultVm columnVm = this.dataToNode(column);
            result.add(columnVm);
        }

        //清洗数据
        String columnName = params.getString("columnName");
        if (!StringUtils.isEmpty(columnName)) {
            result = this.dataCleaning(result, columnName);
        }
        this.setNodeId(result,1);
        return result;
    }

    public boolean existsByTemplateId(String templateId) {
        return columnDao.existsByTemplateId(templateId);
    }
    public boolean existsByParentId(String parentId){
        return this.columnDao.existsByParentId(parentId);
    }

    public boolean existsByParentIdAndColumnName(String parentId,String columnName){
        return this.columnDao.existsByParentIdAndColumnName(parentId,columnName);
    }
    public boolean existsByParentIdAndColumnNameAndIdNot(String parentId,String columnName,String id){
        return this.columnDao.existsByParentIdAndColumnNameAndIdNot(parentId,columnName,id);
    }

    public Column findFirstByColumnId(String columnId){
        return this.columnDao.findFirstByColumnId(columnId);
    }

    public List<String> findAllColumnIdsByParentId(String parentId){
        return this.columnDao.findAllColumnIdsByParentId(parentId);
    }
    /**
     * 查询子节点
     *
     * @param parent 父节点数据
     * @return 查询结果
     */
    private List<ColumnResultVm> getChildren(Column parent) throws DoValidException {
        //查询子节点数据
        List<Column> childrenData = this.columnDao.findAllByParentIdOrderByPower(parent.getId());

        List<ColumnResultVm> children = new Vector<>();

        //遍历子节点数据，转换数据结构，并向下遍历子节点
        for (Column childData : childrenData) {
            ColumnResultVm child = this.dataToNode(childData);
            children.add(child);
        }

        return children;
    }

    /**
     * 转换数据结构，并向下遍历子节点
     *
     * @param data 数据
     * @return 转换后的数据
     */
    private ColumnResultVm dataToNode(Column data) throws DoValidException {
        ColumnResultVm node = new ColumnResultVm();
        BeanUtils.copyProperties(data, node);
        node.setColumnId(node.getId());

        List<ColumnResultVm> children = this.getChildren(data);
        node.setChildren(children);

        node.setHasChildren(true);
        //子节点不存在
        if (children.size() == 0) {
            node.setHasChildren(false);
        }
        return node;
    }

    /**
     * 数据清洗
     *
     * @param data    数据
     * @param keyword 关键字
     * @return 清洗后数据
     */
    private List<ColumnResultVm> dataCleaning(List<ColumnResultVm> data, String keyword) {
        List<ColumnResultVm> tempList = this.dataClone(data);
        if (data.size() > 0) {
            for (ColumnResultVm columnResultVm : data) {
                //向下清洗子节点数据
                List<ColumnResultVm> cleanData = this.dataCleaning(columnResultVm.getChildren(), keyword);

                for (ColumnResultVm temp : tempList) {
                    //匹配数据
                    if (temp.getId().equals(columnResultVm.getId())) {
                        temp.setChildren(cleanData);

                        //清洗后子节点为空，是否有子节点字段  更改为否
                        if (temp.getChildren().size() == 0) {
                            temp.setHasChildren(false);
                        }

                        //叶子节点需要检查栏目名称，不包含关键字的数据需要移除
                        if (!temp.getHasChildren() && !temp.getColumnName().contains(keyword)) {
                            tempList.remove(temp);
                        }
                        break;
                    }
                }
            }
        }

        Integer id = 1;


        return tempList;
    }

    /**
     * 克隆数据
     *
     * @param data 数据
     * @return 克隆后的数据
     */
    private List<ColumnResultVm> dataClone(List<ColumnResultVm> data) {
        List<ColumnResultVm> result = new Vector<>();
        for (ColumnResultVm item : data) {
            ColumnResultVm temp = new ColumnResultVm();
            temp = item.clone();
            result.add(temp);
        }
        return result;
    }



    private void setNodeId(List<ColumnResultVm> data, Integer id) {
        for (ColumnResultVm item : data) {
            item.setColumnId(item.getId());
            item.setId(id.toString());
            if(item.getHasChildren()){
                this.setNodeId(item.getChildren(),id * 10 + 1);
            }
            id++;
        }
    }
}
