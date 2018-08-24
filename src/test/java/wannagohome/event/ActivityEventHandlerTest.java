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
import wannagohome.support.AcceptanceTest;
import wannagohome.support.SpringTest;


import javax.annotation.Resource;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

public class ActivityEventHandlerTest extends SpringTest {

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
}

