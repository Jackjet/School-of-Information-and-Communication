package d1.project.cms.controller.webadmin.website.manage;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.cms.annotation.Permission;
import d1.project.cms.model.website.manage.MenuTreeObjectGN;
import d1.project.cms.service.website.manage.MyServices;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Auth("webadmin")
@Permission("nopermission")
@RestController
@RequestMapping(value = "/webadmin/permission/my")
@Api(value = "/webadmin/permission/my", tags = "我的信息")
public class MyController {

    @Autowired
    private MyServices myServices;

    @ApiOperation("获取用户权限内功能树")
    @GetMapping(value = {"/menuTree"})
    public Result<List<MenuTreeObjectGN>> menuTree(HttpServletRequest request) {
        try {
            List<MenuTreeObjectGN> m = myServices.getMyMenuTree(request);
            return ResultUtil.success("成功", m);
        } catch (Exception e) {
            return ResultUtil.fail( e.getMessage(),e);
        }
    }



}

