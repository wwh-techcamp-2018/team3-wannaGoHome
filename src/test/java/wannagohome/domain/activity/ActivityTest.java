package wannagohome.domain.activity;

import org.junit.Before;
import org.junit.Test;
import wannagohome.component.AES256Encoder;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;

import java.io.UnsupportedEncodingException;

import static org.assertj.core.api.Assertions.assertThat;


public class ActivityTest {

    private User user;
    private Team team;
    private AES256Encoder aes256Encoder;

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

        aes256Encoder = new AES256Encoder("1234567890123456");
    }

    @Test
    public void getTopicTest() {
        AbstractActivity activity = TeamActivity.valueOf(user, team, ActivityType.BOARD_CREATE);
        activity.setReceiver(user);
        assertThat(activity.getTopic(aes256Encoder)).isEqualTo("/topic/user/" + aes256Encoder.encode(user.getEmail()));
    }


    @Test
    public void cloneTest() {
        AbstractActivity activity = TeamActivity.valueOf(user, team, ActivityType.BOARD_CREATE);
        activity.setReceiver(user);
        AbstractActivity cloneActivity = (AbstractActivity) activity.clone();
        assertThat(cloneActivity).isNotEqualTo(activity);
        assertThat(cloneActivity.getReceiver()).isEqualTo(activity.getReceiver());
        assertThat(cloneActivity.getType()).isEqualTo(activity.getType());
    }
}