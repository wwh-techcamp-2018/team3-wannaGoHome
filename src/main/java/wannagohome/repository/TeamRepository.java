package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Team;

public interface TeamRepository extends CrudRepository<Team,Long> {
}
