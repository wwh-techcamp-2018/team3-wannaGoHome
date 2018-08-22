package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Board;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInBoard;

import java.util.List;
import java.util.Optional;

public interface UserIncludedInBoardRepository extends CrudRepository<UserIncludedInBoard, Long> {
    List<UserIncludedInBoard> findAllByUser(User user);

    Optional<UserIncludedInBoard> findByUserAndBoard(User user, Board board);

    List<UserIncludedInBoard> findAllByBoard(Board board);
}
