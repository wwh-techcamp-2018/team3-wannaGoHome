package wannagohome.repository;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import wannagohome.domain.board.RecentlyViewBoard;

import java.util.List;

public interface RecentlyViewBoardRepository extends CrudRepository<RecentlyViewBoard,Long> {
    @Query(value = "select id, user_id, board_id" +
            " from recently_view_board A " +
            "where exists ( " +
                            "select id " +
                            "from ( " +
                                    "select max(id) as id " +
                                    "from recently_view_board " +
                                    "where user_id = :user_id " +
                                    "group by user_id, board_id ) B " +
                            "where A.id = B.id) " +
                            "order by id desc limit 4", nativeQuery = true)
    List<RecentlyViewBoard> findFirst4ByUserOrderByIdDesc(@Param("user_id") Long userId);


}
