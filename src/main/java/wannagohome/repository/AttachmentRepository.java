package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.file.Attachment;

public interface AttachmentRepository extends CrudRepository<Attachment,Long> {

}
