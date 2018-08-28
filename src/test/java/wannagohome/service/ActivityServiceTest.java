package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.domain.activity.ActivityDto;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.BoardActivity;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.BoardRepository;
import wannagohome.repository.TeamRepository;
import wannagohome.repository.UserRepository;
import wannagohome.support.SpringTest;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

public class ActivityServiceTest extends SpringTest {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private ActivityRepository activityRepository;

    private User user;
    private Team team;
    private Board board;
    List<AbstractActivity> activities;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        team = teamRepository.findById(1L).get();

        user = userRepository.findByEmail("songintae@woowahan.com").get();

        board = boardRepository.findById(1L).get();

        AbstractActivity activity1 = BoardActivity.valueOf(user, board, ActivityType.BOARD_CREATE);
        AbstractActivity activity2 = BoardActivity.valueOf(user, board, ActivityType.BOARD_UPDATE);
        activity1.setReceiver(user);
        activity2.setReceiver(user);
        activities = Arrays.asList(
                activity1,
                activity2

        );
        activityRepository.save(activities.get(0));
        activityRepository.save(activities.get(1));
    }


    @Test
    @Transactional
    public void findUserActivities() {
        List<ActivityDto> activities = activityService.findUserActivities(user);
        assertThat(activities.size()).isEqualTo(2);
    }

    @Test
    @Transactional
    public void fetchActivity() {
        List<ActivityDto> activities = activityService.fetchActivity(user, "2220-02-01 14:22:11");
        assertThat(activities.size()).isEqualTo(2);
    }
}