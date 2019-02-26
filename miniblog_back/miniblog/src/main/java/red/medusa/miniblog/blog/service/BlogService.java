package red.medusa.miniblog.blog.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Field;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import red.medusa.miniblog.blog.bean.Blog;

import java.util.List;

import static org.springframework.data.mongodb.core.query.Criteria.where;

@Service
public class BlogService {

    @Autowired
    private MongoTemplate mongoTemplate;

    public List<Blog> getBlogCatalog() {
        Query query = new Query();
        excludeSomeField(query);
        return mongoTemplate.find(query, Blog.class);
    }

    public Blog getBlogPreview(String id) {
        Query query = new Query(where("id").is(id));
        Field fields = query.fields();
        fields.exclude("htmlContent");
        return mongoTemplate.findOne(query, Blog.class);
    }

    public List<Blog> getPreviewByIds(String[] ids) {
        Query query = new Query(where("id").in(ids));
        query.with(new Sort(Sort.Direction.DESC, "updateTime"));
        Field fields = query.fields();
        fields.exclude("htmlContent");
        return mongoTemplate.find(query, Blog.class);
    }

    public List<Blog> getBlogCatalogByKeyword(String keyword) {
        Criteria criteria = new Criteria();
        criteria.orOperator(where("title").regex("(" + keyword + ")+"),
                            where("author").is(keyword),
                            where("tags").is(keyword),
                            where("previewContent").regex("(" + keyword + ")+"),
                            where("htmlContent").regex("(" + keyword + ")+"));
        Query query = new Query(criteria);
        excludeSomeField(query);
        List<Blog> blogs = mongoTemplate.find(query, Blog.class);
        return blogs;
    }

    private void excludeSomeField(Query query) {
        query.with(new Sort(Sort.Direction.DESC, "updateTime"));
        Field fields = query.fields();
        fields.exclude("htmlContent");
        fields.exclude("previewContent");
    }
}
