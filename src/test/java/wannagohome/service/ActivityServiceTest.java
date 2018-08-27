package wannagohome.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wannagohome.domain.ActivityDto;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserRepository;
import wannagohome.support.SpringTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityServiceTest extends SpringTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Test
    public void findUserActivities() {
        User user = userRepository.findByEmail("songintae@woowahan.com").get();
        List<ActivityDto> activities = activityService.findUserActivities(user);
        assertThat(activities.size()).isEqualTo(0);
    }
}