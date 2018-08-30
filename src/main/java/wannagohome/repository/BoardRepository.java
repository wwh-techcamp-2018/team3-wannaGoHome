package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;

import java.util.List;
import java.util.Optional;

public interface BoardRepository extends CrudRepository<Board, Long> {

    List<Board> findAllByTeamAndDeletedFalse(Team team);

    Optional<Board> findByIdAndDeletedFalse(Long id);

}
