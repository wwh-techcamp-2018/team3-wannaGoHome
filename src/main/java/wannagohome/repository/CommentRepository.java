package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Comment;

import java.util.List;

public interface CommentRepository extends CrudRepository<Comment, Long> {

    List<Comment> findAllByDeletedFalse();
}
