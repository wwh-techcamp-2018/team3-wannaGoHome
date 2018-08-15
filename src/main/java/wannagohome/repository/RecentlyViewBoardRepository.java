package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.RecentlyViewBoard;
import wannagohome.domain.User;

import java.util.List;

public interface RecentlyViewBoardRepository extends CrudRepository<RecentlyViewBoard,Long> {
    List<RecentlyViewBoard> findFirst3ByUserOrderByIdDesc(User user);
}
