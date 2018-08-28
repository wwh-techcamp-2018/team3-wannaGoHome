package wannagohome.event;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.BoardActivity;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.repository.*;
import wannagohome.support.SpringTest;

import javax.transaction.Transactional;
import java.io.UnsupportedEncodingException;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;


public class ActivityEventListenerTest extends SpringTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserRepository userRepository;

    private User user;
    private Team team;
    private Board board;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        team = Team.builder()
                .id(1L)
                .name("wannagohome")
                .description("wannagohome 화이팅")
                .profileImage("default.png")
                .build();

        user = userRepository.findByEmail("songintae@woowahan.com").get();

        board = boardRepository.findById(1L).get();
    }

    @Test
    @Transactional
    public void teamEventListenerOnApplicationEventTest() {
        AbstractActivity activity = BoardActivity.valueOf(user, board, ActivityType.BOARD_CREATE);
        applicationEventPublisher.publishEvent(new TeamEvent(this, activity));
        List<AbstractActivity> activities = (List<AbstractActivity>) activityRepository.findAll();
        assertThat(activities.size()).isEqualTo(userIncludedInTeamRepository.findAllByTeam(board.getTeam()).size());
    }


    @Test
    @Transactional
    public void boardEventListenerOnApplicationEventTest() {
        AbstractActivity activity = BoardActivity.valueOf(user, board, ActivityType.BOARD_UPDATE);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        List<AbstractActivity> activities = (List<AbstractActivity>) activityRepository.findAll();
        assertThat(activities.size()).isEqualTo(userIncludedInBoardRepository.findAllByBoard(board).size());
    }


}