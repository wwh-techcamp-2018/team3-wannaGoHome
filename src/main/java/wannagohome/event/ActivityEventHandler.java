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

    private void saveActivity(AbstractActivity activity) {
        AbstractActivity saveActivity = (AbstractActivity) activity.clone();
        saveActivity.setId(null);
        activityRepository.save(saveActivity);
    }

    private void sendMessage(AbstractActivity activity) {
        ActivityDto payload = new ActivityDto(
                activityMessageGenerator.generateMessage(activity)
        );
        simpMessageSendingOperations.convertAndSend(
                "/topic/user/" + encoder.encode(activity.getReceiver().getEmail()),
                payload
        );
    }
}
