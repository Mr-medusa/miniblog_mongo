import Vue from "vue"

const BLOG_TAG_REST = "/miniblog/miniblog/data/blogTag";

function BlogTags() {
    this.get = function (id) {
        return Vue.http.get(BLOG_TAG_REST + "/" + id);
    };

    this.patch = function(tags){
        return Vue.http.patch(BLOG_TAG_REST+ "/"+tags.id, tags);
    };

}
export default BlogTags;