package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Component;
import wannagohome.component.ActivityMessageGenerator;
import wannagohome.domain.CardActivity;
import wannagohome.domain.User;
import wannagohome.repository.ActivityRepository;
import wannagohome.repository.UserIncludedInBoardRepository;

@Component
public class CardEventListener implements ApplicationListener<CardEvent> {

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityRepository activityRepository;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Autowired
    private ActivityMessageGenerator activityMessageGenerator;

    @Override
    public void onApplicationEvent(CardEvent event) {
        CardActivity activity = event.getActivity();
        userIncludedInBoardRepository.findAllByBoard(activity.getBoard())
                .forEach(userIncludedInBoard -> handleEvent(activity, userIncludedInBoard.getUser()));
    }

    private void handleEvent(CardActivity activity, User user) {
        activity.setReceiver(user);
        saveActivity(activity);
        sendMessage(activity);
    }

    private void saveActivity(CardActivity activity) {
        activity.setId(null);
        activityRepository.save(activity);
    }

    private void sendMessage(CardActivity activity) {
        simpMessageSendingOperations.convertAndSend("/topic/activity/board/" + activity.getBoard().getId(), activityMessageGenerator.generateMessage(activity));
    }
}
