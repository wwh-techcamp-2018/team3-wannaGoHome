package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.User;

import java.util.List;

public interface ActivityRepository extends CrudRepository<AbstractActivity, Long> {
    List<AbstractActivity> findFindFirst10ByActivityTypeInAndReceiverOrderByRegisteredDateDesc(List<String> dTypes, User receiver);
}
