package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;

import java.util.List;

public interface BoardRepository extends CrudRepository<Board, Long> {

    List<Board> findAllByTeamAndDeletedFalse(Team team);

}
