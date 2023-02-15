package d1.project.cms.service.repository;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.dao.repository.ResourceFileDao;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author libin
 */
@Service
public class ResourceFileService {
    private final ResourceFileDao resourceFileDao;

    public ResourceFileService(ResourceFileDao resourceFileDao) {
        this.resourceFileDao = resourceFileDao;
    }

    public Page<ResourceFile> findAll(JSONObject params){
        //父id为空时，默认赋值为0
        if(StringUtils.isEmpty(params.getString("parentId"))){
            params.put("parentId","0");
        }

        SpecificationBuilder<ResourceFile> builder = new SpecificationBuilder<>();
        Pageable pageable = this.getPageable(null,params.getInteger("page"),params.getInteger("size"));

        List<ResourceFile> data = this.resourceFileDao.findAllByRepositoryIdAndParentIdOrderByTypeAndCreateTime(params.getString("repositoryId"),params.getString("parentId"));

        return builder.listToPage(data,pageable);
    }

    public Page<ResourceFile> findAllByType(JSONObject params) {
        //父id为空时，默认赋值为0
        if(StringUtils.isEmpty(params.getString("parentId"))){
            params.put("parentId","0");
        }

        SpecificationBuilder<ResourceFile> builder = new SpecificationBuilder<>();
        Pageable pageable = this.getPageable(null,params.getInteger("page"),params.getInteger("size"));

        List<ResourceFile> data = this.resourceFileDao.findAllByRepositoryIdAndParentIdAndTypeOrderByTypeAndCreateTime(params.getString("repositoryId"),params.getString("parentId"),(List<String>)params.get("type"));

        return builder.listToPage(data,pageable);
    }

    public ResourceFile findById(String id) throws DoValidException {
        return this.resourceFileDao.findById(id).orElseThrow(() -> new DoValidException("资源文件不存在"));
    }

    public void save(ResourceFile resourceFile){
        this.resourceFileDao.save(resourceFile);
    }

    public void delete(ResourceFile resourceFile){
        this.resourceFileDao.delete(resourceFile);
    }

    public boolean existsByRepositoryId(String repositoryId){
        return this.resourceFileDao.existsByRepositoryId(repositoryId);
    }

    public boolean existsByParentId(String id){
        return this.resourceFileDao.existsByParentId(id);
    }

    private Pageable getPageable(Sort sort,Integer page,Integer size) {
        page = page != null ? page : 1;
        size = size != null ? size : 10;
        page = page != null && page >= 1 ? page - 1 : 0;
        if (size == null || size < 1) {
            size = 10;
        }

        return sort != null ? PageRequest.of(page, size, sort) : PageRequest.of(page, size);
    }
}
