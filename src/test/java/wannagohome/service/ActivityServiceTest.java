package wannagohome.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserRepository;

import static org.assertj.core.api.Assertions.assertThat;


@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ActivityServiceTest {


    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void findUserActivities() {
        activityRepository.findAll();
        User user = userRepository.findByEmail("songintae@woowahan.com").get();
//        ActivityInitDto activityDto = activityService.findUserActivities(user);
//        assertThat(activityDto.getActivityMessages()).contains("one board 보드를 생성하였습니다.");
    }
}