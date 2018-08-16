package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.User;
import wannagohome.domain.UserIncludedInTeam;

import java.util.List;

public interface UserIncludedInTeamRepository extends CrudRepository<UserIncludedInTeam, Long> {

    List<UserIncludedInTeam> findAllByUser(User user);
}
