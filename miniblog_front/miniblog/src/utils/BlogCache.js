export default {
    blogPages: [],
    maxSize: 10,
    putBlog(blog, count = 0) {
        for (let i = 0; i < this.blogPages.length; i++) {
            var index = this.blogPages[i].blogs.findIndex(v => v.id === blog.id);
            this.blogPages[i].blogs[index] = blog;
            if (count <= 0) //有标签结果集的缓存需要额外搜索一次
                return true;
            count--;
        }
    },
    deleteBlogById(id, newBlog) {
        for (let i = 0; i < this.blogPages.length; i++) {
            var index = this.blogPages[i].blogs.findIndex(v => v.id === id);
            this.blogPages[i].blogs[index] = newBlog;
        }
    },
    getPage(key) {
        for (let i = 0; i < this.blogPages.length; i++) {
            if (key === this.blogPages[i].key) {
                return this.blogPages[i].blogs;
            }
        }
    },
    addBlogPage(key, blogs) {
        this.blogPages.push({
            key: key,
            blogs: blogs
        });
        if (this.blogPages.length >= this.maxSize) {
            this.blogPages.shift();
        }
    },
    deletePageByPrefixKey(prefixKey) {
        for (let i = 0; i < this.blogPages.length; i++) {
            if (this.blogPages[i].key.toString().includes(prefixKey)) {
                this.blogPages.splice(i, 1);
                i = i - 1 >= 0 ? i - 1 : 0;
            }
        }
    }

}