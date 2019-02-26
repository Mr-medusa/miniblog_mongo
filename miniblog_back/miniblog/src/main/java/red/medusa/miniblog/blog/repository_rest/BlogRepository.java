
package red.medusa.miniblog.blog.repository_rest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import red.medusa.miniblog.blog.bean.Blog;

@RepositoryRestResource(collectionResourceRel = "blog", path = "blog")
public interface BlogRepository extends MongoRepository<Blog, String> {}
