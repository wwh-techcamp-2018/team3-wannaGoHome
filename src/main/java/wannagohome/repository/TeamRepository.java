package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.Team;

import java.util.List;


public interface TeamRepository extends JpaRepository<Team, Long> {
}
