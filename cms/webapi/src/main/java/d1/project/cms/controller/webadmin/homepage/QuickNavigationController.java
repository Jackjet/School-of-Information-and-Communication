package d1.project.cms.controller.webadmin.homepage;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.Constants;
import d1.project.cms.business.HomePage;
import d1.project.cms.entity.homepage.QuickNavigation;
import d1.project.cms.model.homepage.OsInfoVm;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author baozh
 */
@Auth("webadmin")
@RestController
@RequestMapping("/webadmin/homePage")
@Api(value = "/webadmin/homePage", tags = "首页")
public class QuickNavigationController {
    private final HomePage homePage;

    public QuickNavigationController(HomePage homePage) {
        this.homePage = homePage;
    }

    @ApiOperation(value = "查询", notes = "查询用户所有导航")
    @RequestMapping(value = "/getNavigation", method = RequestMethod.GET)
    public Result<List<QuickNavigation>> findAll(HttpServletRequest request) {
        try {
            return ResultUtil.success(Constants.SUCCESS,homePage.getNavigation(request));
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "查询", notes = "查询用户所有导航")
    @RequestMapping(value = "/getUserMenus", method = RequestMethod.GET)
    public Result<List<QuickNavigation>> getUserMenus(HttpServletRequest request) {
        try {
            return ResultUtil.success(Constants.SUCCESS,homePage.getUserMenu(request));
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "添加", notes = "添加用户导航")
    @RequestMapping(value = "/insert", method = RequestMethod.POST)
    public Result<String> insert(@RequestBody List<String> list, HttpServletRequest request) {
        try {
            homePage.insert(list,request);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "删除", notes = "删除用户导航")
    @RequestMapping(value = "/delete", method = RequestMethod.DELETE)
    public Result<String> delete(String id) {
        try {
            homePage.deleteById(id);
            return ResultUtil.success(Constants.SUCCESS);
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }

    @ApiOperation(value = "获取服务器信息", notes = "获取服务器信息")
    @RequestMapping(value = "/osInfo", method = RequestMethod.GET)
    public Result<OsInfoVm> osInfo() {
        try {
            return ResultUtil.success(Constants.SUCCESS,homePage.getOsInfo());
        } catch (Exception e) {
            return ResultUtil.fail(Constants.FAIL + e.getMessage(), e);
        }
    }
}
