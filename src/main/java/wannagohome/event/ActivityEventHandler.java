package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.domain.user.User;
import wannagohome.repository.ActivityRepository;

import javax.annotation.Resource;
import java.util.Arrays;
import java.util.List;

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
        saveActivity.clearId();
        activityRepository.save(saveActivity);
    }

    public void sendMessage(AbstractActivity activity) {
        simpMessageSendingOperations.convertAndSend(
                activity.getTopic(encoder),
                Arrays.asList(activityMessageGenerator.generateMessage(activity))
        );
    }

    public void sendPersonalMessage(User user, List<AbstractActivity> activities) {
        String topic = "/topic/user/" + encoder.encode(user.getEmail());
        simpMessageSendingOperations.convertAndSend(
                topic,
                activities.stream().map(activity -> activityMessageGenerator.generateMessage(activity))
        );
    }
}
