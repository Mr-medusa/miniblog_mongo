package red.medusa.miniblog.blog.bean;

import java.util.List;

public class BlogTag {

    private String id;
    private List<List<Object>> tags;

    public BlogTag() {
    }

    public BlogTag(String id, List<List<Object>> tags) {
        this.id = id;
        this.tags = tags;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public List<List<Object>> getTags() {
        return tags;
    }

    public void setTags(List<List<Object>> tags) {
        this.tags = tags;
    }
}
