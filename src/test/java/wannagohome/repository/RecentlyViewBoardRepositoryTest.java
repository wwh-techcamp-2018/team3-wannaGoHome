package wannagohome.repository;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wannagohome.domain.RecentlyViewBoard;
import wannagohome.domain.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class RecentlyViewBoardRepositoryTest {

    @Autowired
    RecentlyViewBoardRepository recentlyViewBoardRepository;

    @Test
    public void findFirst3ByUserOrderByIdDesc_조회결과_없을경우() {
        List<RecentlyViewBoard> recentlyViewBoards = recentlyViewBoardRepository
                .findFirst4ByUserOrderByIdDesc(User.builder().id(1L).build());
        assertThat(recentlyViewBoards).isNotNull();
        assertThat(recentlyViewBoards.size()).isEqualTo(0);
    }

}