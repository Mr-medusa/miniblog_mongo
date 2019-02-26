export default {
    blogs: new Map(),
    maxSize: 50,
    putBlog(blog) {
        this.blogs.set(blog.id, blog);
        if (this.blogs.size >= this.maxSize)
            this.blogs.delete(this.blogs.keys().next().value);
    },
    getBlog(id) {
        return this.blogs.get(id);
    }
}