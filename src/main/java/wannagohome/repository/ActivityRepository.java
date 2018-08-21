package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.AbstractActivity;

public interface ActivityRepository extends CrudRepository<AbstractActivity, Long> {
}
