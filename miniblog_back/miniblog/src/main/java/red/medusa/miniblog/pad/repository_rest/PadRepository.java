
package red.medusa.miniblog.pad.repository_rest;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import red.medusa.miniblog.pad.bean.Pad;

@RepositoryRestResource(collectionResourceRel = "pad", path = "pad")
public interface PadRepository extends MongoRepository<Pad, String> {}
