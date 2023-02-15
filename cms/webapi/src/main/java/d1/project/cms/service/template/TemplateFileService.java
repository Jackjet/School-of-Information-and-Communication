package d1.project.cms.service.template;

import d1.framework.webapi.configuration.DoValidException;
import d1.project.cms.Constants;
import d1.project.cms.model.template.FileContent;
import d1.project.cms.model.template.FileList;
import d1.project.cms.model.template.InsertFile;
import net.dcrun.component.file.server.FileServerService;
import net.dcrun.component.file.util.FileUtilService;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * @author baozh
 */
@Service
public class TemplateFileService {

    private final FileUtilService fileUtilService;
    private final FileServerService fileServerService;

    public TemplateFileService(FileUtilService fileUtilService, FileServerService fileServerService) {
        this.fileUtilService = fileUtilService;
        this.fileServerService = fileServerService;
    }

    /**
     * 获取文件目录
     * @param url 文件父级目录
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public List<FileList> getFiles(String url) throws Exception{
        if (StringUtils.isEmpty(url)){
            File file = new File(Constants.TEMPLATE_ROOT);
            createFile(file);
            String pcUrl = Constants.TEMPLATE_ROOT+File.separator+"pc";
            File pcFile = new File(pcUrl);
            createFile(pcFile);
            String appUrl = Constants.TEMPLATE_ROOT+File.separator+"app";
            File appFile = new File(appUrl);
            createFile(appFile);
            List<FileList> lists = new ArrayList<>();
            return getFileList(file,lists);
        }else {
            url = url.replace('/',File.separatorChar);
            url = Constants.TEMPLATE_ROOT+url;

            File file = new File(url);
            List<FileList> lists = new ArrayList<>();
            return getFileList(file,lists);
        }
    }

    /**
     * 获取文件详情
     * @param url 文件地址
     * @return 查询结果
     */
    public FileContent getContent(String url,String name)throws Exception{
        String allUrl = Constants.TEMPLATE_ROOT+url+File.separator+name;
        File file = new File(allUrl);
        if (!file.exists()){
            throw new DoValidException(Constants.FILE_NOT_EXISTS);
        }
//        List<String> list = new ArrayList<>();
//        InputStreamReader inputReader = new InputStreamReader(new FileInputStream(file));
//        BufferedReader bf = new BufferedReader(inputReader);
//        // 按行读取字符串
//        String str;
//        while ((str = bf.readLine()) != null) {
//            list.add(str);
//        }
//        bf.close();
//        inputReader.close();

        FileContent fileContent = new FileContent();
        fileContent.setFileName(name);
        fileContent.setUrl(url);
        fileContent.setContent(fileUtilService.readLines(allUrl,"utf-8"));
        return fileContent;
    }

    /**
     * 添加或编辑文件
     * @param insertFile 添加或编辑文件信息
     * @throws Exception 向上抛出异常
     */
    public void insertOrUpdateFile(InsertFile insertFile) throws Exception{
        String url = insertFile.getUrl();
        String name = insertFile.getName();
        List<Object> content = insertFile.getContent();
        int lineNum = content.size();
        if (lineNum > 0){
            String allUrl = Constants.TEMPLATE_ROOT+url+File.separator+name+".html";
            File file = new File(allUrl);
            if (!file.exists()){
                boolean is = file.createNewFile();
                if (!is){
                    throw new DoValidException(Constants.FILE_CREATE_FAIL);
                }
            }
            fileUtilService.writeLinesWithParam(allUrl,content, "UTF-8",false);
        }else {
            String allUrl = Constants.TEMPLATE_ROOT+url+File.separator+name;
            File file = new File(allUrl);
            boolean is = file.mkdirs();
            if (!is){
                throw new DoValidException(Constants.FILE_CREATE_FAIL);
            }
        }
    }

    public void uploadFile(MultipartFile[] multipartFiles,String url) throws Exception{
        url = url.replace('/',File.separatorChar);
        String allUrl = Constants.TEMPLATE_ROOT+url;
        fileServerService.uploadFiles(multipartFiles,allUrl,allUrl);
    }

    /**
     *  创建文件夹
     * @param file 文件夹信息
     * @throws Exception 无法创建抛出异常
     */
    private void createFile(File file) throws Exception{
        if (!file.exists()){
            boolean rel = file.mkdirs();
            if (!rel){
                throw new DoValidException(Constants.FILE_CREATE_FAIL);
            }
        }
    }

    /**
     * 递归查询文件夹列表
     * @param file 主文件夹
     * @param lists 结果集合
     * @return 查询结果
     */
    private List<FileList> getFileList( File file,List<FileList> lists){
        File[] array = file.listFiles();
        if (array != null && array.length>=1){
            for (File file1 : array) {
                FileList fileList = new FileList();
                fileList.setType("0");
                fileList.setName(file1.getName());
                //当file1不是文件时，递归下次file1下的所有文件
                if (!file1.isFile()){
                    fileList.setType("1");
                }
                lists.add(fileList);
            }
        }
        return lists;
    }
}
