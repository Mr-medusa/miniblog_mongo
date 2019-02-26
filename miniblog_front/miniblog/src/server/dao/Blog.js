import Vue from "vue"

const BLOG_REST = "/miniblog/miniblog/data/blog";
const BLOG_CATALOG = "/miniblog/getBlogCatalog";
const BLOG_CATALOG_KEYWORD = "/miniblog/getBlogCatalogByKeyword";

function Blog() {

    this.get = function (id) {
        return Vue.http.get(BLOG_REST + "/" + id);
    };
    this.getAll = function () {
        return Vue.http.get(BLOG_CATALOG);
    };
    this.put = function (blog) {
        return Vue.http.put(BLOG_REST + "/" + blog.id, blog);
    };

    this.delete = function (id) {
        return Vue.http.delete(BLOG_REST + "/" + id);
    };
    this.getBlogCatalogByKeyword = function (keyword) {
        return Vue.http.get(BLOG_CATALOG_KEYWORD, {params: {keyword: keyword}});
    };
}

export default Blog;