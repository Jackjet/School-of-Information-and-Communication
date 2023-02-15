package d1.project.cms.business;

import com.alibaba.fastjson.JSONObject;
import d1.framework.webapi.configuration.DoValidException;
import d1.framework.webapi.utils.TokenService;
import d1.project.cms.Constants;
import d1.project.cms.entity.repository.Repository;
import d1.project.cms.entity.repository.ResourceFile;
import d1.project.cms.enums.ResourceFileType;
import d1.project.cms.service.repository.RepositoryService;
import d1.project.cms.service.repository.ResourceFileService;
import d1.project.cms.utils.MyUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.File;

/**
 * @author libin
 */
@Service
public class RepositoryBusiness {
    private final RepositoryService repositoryService;
    private final ResourceFileService resourceFileService;
    private final TokenService tokenService;

    public RepositoryBusiness(RepositoryService repositoryService, ResourceFileService resourceFileService, TokenService tokenService) {
        this.repositoryService = repositoryService;
        this.resourceFileService = resourceFileService;
        this.tokenService = tokenService;
    }

    public Page<Repository> findAll(JSONObject params) throws DoValidException {
        return this.repositoryService.findAll(params);
    }

    public Repository findById(String id) throws DoValidException {
        return this.repositoryService.findById(id);
    }

    public void insert(HttpServletRequest request, JSONObject params) throws DoValidException {
        Repository repository = JSONObject.toJavaObject(params, Repository.class);
        if(this.repositoryService.existsByName(repository.getName())){
            throw new DoValidException("已存在相同的资源库");
        }

        repository.setId(MyUtils.generate32Id());
        this.tokenService.updateCreateIdAndTime(request, repository);

        this.repositoryService.save(repository);
    }

    public void update(JSONObject params) throws DoValidException {
        Repository repository = JSONObject.toJavaObject(params, Repository.class);

        if(this.repositoryService.existsByNameAndIdNot(repository.getName(),repository.getId())){
            throw new DoValidException("已存在相同的资源库");
        }

        Repository data = this.repositoryService.findById(repository.getId());

        data.setName(repository.getName());
        this.repositoryService.save(data);
    }

    public void deleteById(String id) throws DoValidException {
        if(this.resourceFileService.existsByRepositoryId(id)){
            throw new DoValidException("抱歉，文件夹下包含资源请勿删除");
        }

        Repository data = this.repositoryService.findById(id);

        this.repositoryService.delete(data);
    }

    public Page<ResourceFile> fileFindAll(JSONObject params) {
        return this.resourceFileService.findAll(params);
    }

    public void fileInsert(HttpServletRequest request, JSONObject params) {
        ResourceFile resourceFile = JSONObject.toJavaObject(params, ResourceFile.class);
        //顶级资源文件，父id默认0
        if(StringUtils.isEmpty(resourceFile.getParentId())){
            resourceFile.setParentId("0");
        }
        resourceFile.setId(MyUtils.generate32Id());
        this.tokenService.updateCreateIdAndTime(request, resourceFile);

        this.resourceFileService.save(resourceFile);
    }

    @Transactional(rollbackFor = DoValidException.class)
    public void fileDelete(String id) throws DoValidException {
        ResourceFile data = this.resourceFileService.findById(id);
        if(this.resourceFileService.existsByParentId(id) && ResourceFileType.FOLDER.getName().equals(data.getType())){
            throw new DoValidException("当前目录下存在文件或目录");
        }

        this.resourceFileService.delete(data);

        if(!ResourceFileType.FOLDER.getName().equals(data.getType())) {
            if (data.getFileDownloadUri() == null) {
                throw new DoValidException("下载路径异常");
            }

            String[] arr = data.getFileDownloadUri().split("/");
            if (arr.length < 2) {
                throw new DoValidException("下载路径异常");
            }

            String path = Constants.ABSOLUTE_RESOURCE_FILE + arr[arr.length - 2] + File.separator;
            File folder = new File(path);

            File file = new File(path + arr[arr.length - 1]);

            if (file.exists()) {
                file.delete();
            }

            if (folder.isDirectory()) {
                folder.delete();
            }
        }
    }
}
