import EventHub from "../../utils/EventHub"
import Blog from "../dao/Blog"
import blogContent from "../dao/BlogConent"
import BlogTags from "../dao/BlogTags"

var blogDao = new Blog();
var blogContentDao = new blogContent();
var blogTagsDao = new BlogTags();

export default {

    getBlogById(id) {
        return blogDao.get(id);
    },

    getBlogContentByIds(ids) {
        return blogContentDao.getPreviewByIds(ids);
    },

    getBlogCatalog() {
        return blogDao.getAll();
    },

    getBlogTags(id) {
        return blogTagsDao.get(id);
    },

    putBlogAndTags(blog, tags, res) {
        var that = this;
        return blogDao.put(blog).then(function () {
            if (tags)
                that.putBlogTags(tags);
        }).then(() => {
            EventHub.$emit('goTip', [res.successful, true])
        }).catch((err) => {
            EventHub.$emit('goTip', [res.unsuccessful, false]);
            console.log(err);
        });
    },

    putBlogTags(tags) {
        var tagArr = [], entry;
        var mapIter = tags.entries();
        while ((entry = mapIter.next().value)) {
            tagArr.push(entry)
        }
        blogTagsDao.patch({id: "blogTags", tags: tagArr});
    },

    deleteBlog(id, res) {
        return blogDao.delete(id).then(function () {
            EventHub.$emit('goTip', [res.successful, true])
        }).catch(function (arr) {
            console.log(arr);
            EventHub.$emit('goTip', [res.unsuccessful, false])
        });
    },

    getBlogCatalogByKeyword(keyword) {
        return blogDao.getBlogCatalogByKeyword(keyword);
    },
}

