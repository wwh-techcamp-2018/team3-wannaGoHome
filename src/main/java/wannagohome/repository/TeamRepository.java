package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.Team;

import java.util.List;
import java.util.Optional;


public interface TeamRepository extends JpaRepository<Team, Long> {

    Optional<Team> findByIdAndDeletedFalse(Long id);

}
