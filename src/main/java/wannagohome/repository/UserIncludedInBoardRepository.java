package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Board;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInBoard;

import java.util.Optional;

public interface UserIncludedInBoardRepository extends CrudRepository<UserIncludedInBoard, Long> {
    void deleteByBoard(Board board);

    Optional<UserIncludedInBoard> findByUserAndBoard(User user, Board board);
}
