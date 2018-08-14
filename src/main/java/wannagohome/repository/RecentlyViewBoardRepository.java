package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.RecentlyViewBoard;

public interface RecentlyViewBoardRepository extends CrudRepository<RecentlyViewBoard,Long> {
}
