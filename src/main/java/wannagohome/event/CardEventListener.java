package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.CardActivity;
import wannagohome.repository.UserIncludedInBoardRepository;

@Component
public class CardEventListener implements ApplicationListener<CardEvent> {

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityEventHandler activityEventHandler;

    @Override
    public void onApplicationEvent(CardEvent event) {
        CardActivity activity = event.getActivity();
        userIncludedInBoardRepository.findAllByBoard(activity.getBoard())
                .forEach(userIncludedInBoard -> activityEventHandler.handleEvent(activity, userIncludedInBoard.getUser()));
    }

}
