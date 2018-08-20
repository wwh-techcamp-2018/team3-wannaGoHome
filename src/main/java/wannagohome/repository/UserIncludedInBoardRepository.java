package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Board;
import wannagohome.domain.UserIncludedInBoard;

public interface UserIncludedInBoardRepository extends CrudRepository<UserIncludedInBoard, Long> {
    void deleteByBoard(Board board);
}
