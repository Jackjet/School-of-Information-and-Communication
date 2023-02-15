package d1.project.cms.service.repository;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.SpecificationBuilder;
import d1.project.cms.dao.repository.RepositoryDao;
import d1.project.cms.entity.SampleEntity;
import d1.project.cms.entity.repository.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

/**
 * @author libin
 */
@Service
public class RepositoryService {
    private final RepositoryDao repositoryDao;

    public RepositoryService(RepositoryDao repositoryDao) {
        this.repositoryDao = repositoryDao;
    }

    public Page<Repository> findAll(JSONObject params) throws DoValidException {
        try {
            SpecificationBuilder<Repository> builder = new SpecificationBuilder<>();
            Specification<Repository> specification = builder.init(params)
                    .sContain("name", "name")
                    .dOrder("createTime").build();

            return this.repositoryDao.findAll(specification, builder.getPageable());
        }catch (Exception e){
            throw new DoValidException(e.getMessage());
        }
    }

    public void save(Repository repository){
        this.repositoryDao.save(repository);
    }

    public Repository findById(String id) throws DoValidException {
        return this.repositoryDao.findById(id).orElseThrow(()-> new DoValidException("资源库不存在"));
    }

    public void delete(Repository repository){
        this.repositoryDao.delete(repository);
    }

    public boolean existsByName(String name){
        return this.repositoryDao.existsByName(name);
    }

    public boolean existsByNameAndIdNot(String name,String id){
        return this.repositoryDao.existsByNameAndIdNot(name,id);
    }
}
