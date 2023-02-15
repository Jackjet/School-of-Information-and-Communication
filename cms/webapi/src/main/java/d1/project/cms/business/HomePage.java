package d1.project.cms.business;
import java.util.ArrayList;
import java.util.Calendar;

import com.alibaba.fastjson.JSONObject;
import d1.project.cms.entity.homepage.QuickNavigation;
import d1.project.cms.model.homepage.OsInfoVm;
import d1.project.cms.model.website.manage.MenuTreeGN;
import d1.project.cms.model.website.manage.MenuTreeObjectGN;
import d1.project.cms.service.WebAdminService;
import d1.project.cms.service.homepage.QuickNavigationService;
import d1.project.cms.service.website.manage.MyServices;
import d1.project.cms.utils.MyUtils;
import net.dcrun.component.osinfo.OsInfoService;
import net.dcrun.component.osinfo.model.IpInfo;
import net.dcrun.component.osinfo.model.OsInfo;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * 首页
 * @author baozh
 */
@Service
public class HomePage {
    private final QuickNavigationService navigationService;
    private final WebAdminService webAdminService;
    private final OsInfoService osInfoService;
    private final MyServices myServices;

    public HomePage(QuickNavigationService navigationService, WebAdminService webAdminService, OsInfoService osInfoService, MyServices myServices) {
        this.navigationService = navigationService;
        this.webAdminService = webAdminService;
        this.osInfoService = osInfoService;
        this.myServices = myServices;
    }

    /**
     * 获取当前用户的快捷导航列表
     * @param request 请求信息
     * @return 查询结果列表
     * @throws Exception 初始化查询语句异常
     */
    public List<QuickNavigation> getNavigation(HttpServletRequest request) throws Exception {
        String userId = webAdminService.getUserId(request);
        JSONObject json = new JSONObject(1);
        json.put("userId",userId);
        return navigationService.find(json);
    }

    /**
     * 获取该用户所有权限主菜单
     * @param request 请求信息
     * @return 查询结果
     * @throws Exception 向上抛出异常
     */
    public List<QuickNavigation> getUserMenu(HttpServletRequest request) throws Exception{
        List<MenuTreeObjectGN> menus = myServices.getMyMenuTree(request);
        String userId = webAdminService.getUserId(request);
        List<QuickNavigation> returnList = new ArrayList<>();
        getAllList(returnList,menus,userId);
        return returnList;
    }

    /**
     * 添加数据
     * @param list 添加数据
     * @param request 请求
     */
    public void insert(List<String> list, HttpServletRequest request){
        String userId = webAdminService.getUserId(request);
        navigationService.deleteByUserId(userId);
        for (String str : list) {
            String[] strArray = str.split("[,]");
            String apiName = strArray[1];
            String apiUrl = strArray[0];
            QuickNavigation navigation = new QuickNavigation();
            navigation.setApiName(apiName);
            navigation.setApiUrl(apiUrl);
            navigation.setUserId(userId);
            navigation.setCreateById(userId);
            navigation.setCreateTime(Calendar.getInstance());
            navigation.setId(MyUtils.generate32Id());
            navigationService.insert(navigation);

        }
    }

    public void deleteById(String id){
        navigationService.deleteById(id);
    }

    public OsInfoVm getOsInfo() throws Exception {
        IpInfo ipInfo = osInfoService.getIpInfo();
        OsInfo osInfo = osInfoService.getOsInfo();
        OsInfoVm osInfoVm = new OsInfoVm();
        osInfoVm.setOsInfo(osInfo);
        osInfoVm.setIpInfo(ipInfo.getWanIp());
        return osInfoVm;
    }

    public void getAllList(List<QuickNavigation> returnList,List<MenuTreeObjectGN> menus,String userId){
        String homePage = "首页";
        for (MenuTreeObjectGN menu : menus) {
            List<MenuTreeObjectGN> mList = menu.getChilds();
            if (mList.size()>0){
                getAllList(returnList,mList,userId);
            }else {
                MenuTreeGN menuTreeGn = menu.getEntity();
                String name = menuTreeGn.getName();
                if (homePage.equals(name)){
                    continue;
                }
                String url = menuTreeGn.getRoute();
                QuickNavigation quickNavigation = new QuickNavigation();
                if (!StringUtils.isEmpty(name) && !StringUtils.isEmpty(url)){
                    quickNavigation.setApiName(name);
                    quickNavigation.setApiUrl(url);
                    quickNavigation.setUserId(userId);
                }
                returnList.add(quickNavigation);
            }
        }
    }
}
