package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.User;

import java.util.Date;
import java.util.List;

public interface ActivityRepository extends CrudRepository<AbstractActivity, Long> {
    List<AbstractActivity> findFirst10ByReceiverOrderByRegisteredDateDesc(User receiver);
    List<AbstractActivity> findFirst10ByReceiverAndRegisteredDateLessThanOrderByRegisteredDateDesc(User user, Date registeredDate);
    List<AbstractActivity> findFirst10ByReceiver(User user);
}
