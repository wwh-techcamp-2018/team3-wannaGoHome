package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Team;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInTeam;

import java.util.List;
import java.util.Optional;

public interface UserIncludedInTeamRepository extends CrudRepository<UserIncludedInTeam, Long> {
    List<UserIncludedInTeam> findAllByUser(User user);
    Optional<UserIncludedInTeam> findByUserAndTeam(User user, Team team);

    List<UserIncludedInTeam> findAllByTeam(Team team);
}
