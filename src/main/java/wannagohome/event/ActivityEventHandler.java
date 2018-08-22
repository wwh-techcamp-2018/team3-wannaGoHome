package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.ActivityDto;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;

import javax.annotation.Resource;

@Component
public class ActivityEventHandler {

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder encoder;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Autowired
    private ActivityRepository activityRepository;


    public void handleEvent(AbstractActivity activity, User user) {
        activity.setReceiver(user);
        saveActivity(activity);
        sendMessage(activity);
    }

    public void sendPersonalMessage(AbstractActivity activity) {
        simpMessageSendingOperations.convertAndSend(
                "/topic/user/" + encoder.encode(activity.getSource().getEmail()),
                activity.getSubscribeTopicUrl()
        );
    }

    private void saveActivity(AbstractActivity activity) {
        activity.setId(null);
        activityRepository.save(activity);
    }

    private void sendMessage(AbstractActivity activity) {
        // TODO: 2018. 8. 22. MessageDto 만들기
        ActivityDto dto = new ActivityDto(
                activity.getTopicUrl(),
                activityMessageGenerator.generateMessage(activity)
        );
        simpMessageSendingOperations.convertAndSend(activity.getTopicUrl(), dto);
    }
}
