package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.User;

import java.util.Optional;

public interface TeamInviteRepository extends JpaRepository<TeamInvite, Long> {

    Optional<TeamInvite> findByMemberEqualsAndTeamEquals(User member, Team team);

}
