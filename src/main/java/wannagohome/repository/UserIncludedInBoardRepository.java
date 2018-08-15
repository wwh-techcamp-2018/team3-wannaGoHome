package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.UserIncludedInBoard;

public interface UserIncludedInBoardRepository extends CrudRepository<UserIncludedInBoard, Long> {
}
