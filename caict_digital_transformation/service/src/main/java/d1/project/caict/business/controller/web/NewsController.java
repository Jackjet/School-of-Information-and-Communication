package d1.project.caict.business.controller.web;

import d1.framework.webapi.annotation.Auth;
import d1.framework.webapi.http.Result;
import d1.framework.webapi.http.ResultUtil;
import d1.project.caict.business.entity.es.News;
import d1.project.caict.business.service.es.NewsService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author: maorui
 * @date: 2020/9/23
 */

@RestController
@RequestMapping("/web/news")
public class NewsController {
    private final NewsService newsService;

    public NewsController(NewsService newsService) {
        this.newsService = newsService;
    }

    @GetMapping(value = "/findAll")
    public Result<Page<News>> findAll() {
        return ResultUtil.success("", newsService.getAll());
    }

    @GetMapping(value = "/find")
    public Result<News> find(String name) {
        return ResultUtil.success("", newsService.getByName(name));
    }

    @Auth("noauth")
    @GetMapping(value = "/search")
    public Result<Page<News>> search(String keyword, @PageableDefault(sort = "power", direction = Sort.Direction.DESC) Pageable pageable) {
        return ResultUtil.success("", newsService.pageQueryWithHighlight(keyword, pageable));
    }

    @PostMapping(value = "/batchSave")
    public Result<String> batchSave(@RequestBody List<News> news) {
        newsService.batchSave(news);
        return ResultUtil.success();
    }
}
