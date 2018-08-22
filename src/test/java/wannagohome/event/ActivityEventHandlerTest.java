package wannagohome.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;
import wannagohome.domain.*;


import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Resource(name = "activityMessageSourceAccessor")
    MessageSourceAccessor messageSourceAccessor;

    @MockBean
    private BoardEventListener activityEventListener;

    @Test
    public void publishEvent() {
        TeamActivity activity = TeamActivity.valueOf(User.GUEST_USER, new Team(), ActivityType.BOARD_CREATE);
        applicationEventPublisher.publishEvent(new BoardEvent(this, activity));
        verify(activityEventListener, times(1)).onApplicationEvent(any());
    }

    @Test
    public void getMessageByCode() {
        String message = messageSourceAccessor.getMessage(ActivityType.BOARD_UPDATE.getCode(), new String[]{"junsulime"});
        assertThat(message).isEqualTo("junsulime 보드를 수정하였습니다.");
    }
}