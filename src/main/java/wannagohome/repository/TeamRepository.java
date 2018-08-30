package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.team.Team;

import java.util.Optional;

public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByIdAndDeletedFalse(Long id);

    Optional<Team> findByNameAnAndDeletedFalse(String name);
}
