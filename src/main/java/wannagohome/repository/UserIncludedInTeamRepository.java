package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserIncludedInTeam;

import java.util.List;
import java.util.Optional;

public interface UserIncludedInTeamRepository extends CrudRepository<UserIncludedInTeam, Long> {
    List<UserIncludedInTeam> findAllByUserAndTeamDeletedFalse(User user);
    Optional<UserIncludedInTeam> findByUserAndTeam(User user, Team team);

    List<UserIncludedInTeam> findAllByTeamNotAndUserNameContainingIgnoreCase(Team ignoreTeam, String keyword);
    List<UserIncludedInTeam> findAllByTeamNotAndUserEmailContainingIgnoreCase(Team ignoreTeam, String keyword);
    List<UserIncludedInTeam> findAllByTeam(Team team);
}
