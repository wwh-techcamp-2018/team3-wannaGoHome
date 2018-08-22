package wannagohome.event;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.test.context.junit4.SpringRunner;
import wannagohome.domain.BoardActivity;


import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;


@RunWith(SpringRunner.class)
@SpringBootTest
public class ActivityEventHandlerTest {

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @MockBean
    private BoardEventListener activityEventListener;

    @Test
    public void publishEvent() {
//        applicationEventPublisher.publishEvent(new BoardEvent(this, ));
//
//        verify(activityEventListener, times(1)).onApplicationEvent(any());
    }


}