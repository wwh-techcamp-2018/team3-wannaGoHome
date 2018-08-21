package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import wannagohome.domain.*;
import wannagohome.repository.ActivityRepository;

import java.util.Arrays;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.class)
public class ActivityServiceMockTest {

    @Mock
    private ActivityRepository activityRepository;

    @InjectMocks
    private ActivityService activityService;

    private User user;
    private Team team;
    private List<Board> boards;

    @Before
    public void setUp() throws Exception {
        team = Team.builder()
                .id(1L)
                .name("wannagohome")
                .description("wannagohome 화이팅")
                .profileImage("default.png")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password("password1")
                .build();

        boards = Arrays.asList(
                Board.builder().id(1L).title("one board").team(team).build(),
                Board.builder().id(2L).title("two board").team(team).build(),
                Board.builder().id(3L).title("three board").team(team).build(),
                Board.builder().id(4L).title("four board").team(team).build()
        );
    }


    @Test
    public void create() {
        BoardActivity activity = BoardActivity.builder()
                .board(boards.get(0))
                .build();
        when(activityRepository.save(activity)).thenReturn(activity);
        activityService.create(activity);
        verify(activityRepository, times(1)).save(any());
    }
}