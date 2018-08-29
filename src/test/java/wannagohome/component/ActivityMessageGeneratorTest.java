package wannagohome.component;

import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.TeamActivity;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;
import wannagohome.support.SpringTest;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;


public class ActivityMessageGeneratorTest extends SpringTest {


    @Autowired
    ActivityMessageGenerator activityMessageGenerator;

    private User user;
    private Team team;

    @Before
    public void setUp() throws UnsupportedEncodingException {
        team = Team.builder()
                .id(1L)
                .name("wannagohome")
                .description("wannagohome 화이팅")
                .profile("default.png")
                .build();

        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password("password1")
                .build();
    }

    @Test
    public void generateMessageTest() {
        AbstractActivity activity = TeamActivity.valueOf(user, team, ActivityType.BOARD_CREATE);
        assertThat(activityMessageGenerator.generateMessage(activity).getMessage()).isEqualTo("jhyang님이 wannagohome팀에 보드를 생성하였습니다.");
    }
}