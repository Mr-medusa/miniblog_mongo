package red.medusa.miniblog.blog.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import red.medusa.miniblog.blog.bean.Blog;
import red.medusa.miniblog.blog.service.BlogService;

import java.util.List;

@Controller
public class BlogController {

    @Autowired
    private BlogService blogService;

    /**
     * 获取blog目录
     * @return
     */
    @RequestMapping(value = "/getBlogCatalog",method = RequestMethod.GET)
    @ResponseBody
    public List<Blog> getBlogCatalog(){
        return blogService.getBlogCatalog();
    }

    /**
     * 一篇blog的预览
     * @param id
     * @return
     */
    @RequestMapping(value = "/getBlogPreview",method = RequestMethod.GET)
    @ResponseBody
    public Blog getBlogPreview(String id){
        return blogService.getBlogPreview(id);
    }

    /**
     * 根据分页传递的所需一页的id数
     * @param ids
     * @return
     */
    @RequestMapping(value = "/getPreviewByIds",method = RequestMethod.POST)
    @ResponseBody
    public List<Blog> getPreviewByIds(@RequestBody String[] ids){
        return blogService.getPreviewByIds(ids);
    }

    /**
     * 关键字查询
     * @param keyword
     * @return
     */
    @RequestMapping(value = "/getBlogCatalogByKeyword",method = RequestMethod.GET)
    @ResponseBody
    public List<Blog> getBlogCatalogByKeyword(String keyword){
        return blogService.getBlogCatalogByKeyword(keyword);
    }
}
