
package red.medusa.miniblog.blog.repository_rest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import red.medusa.miniblog.blog.bean.BlogTag;

@RepositoryRestResource(collectionResourceRel = "blogTag", path = "blogTag")
public interface BlogTagRepository extends MongoRepository<BlogTag, String> {}
