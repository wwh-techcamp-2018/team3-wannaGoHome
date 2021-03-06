package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.activity.AbstractActivity;
import wannagohome.repository.UserIncludedInBoardRepository;

@Component
public class BoardEventListener implements ApplicationListener<BoardEvent> {

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ActivityEventHandler activityEventHandler;

    @Override
    public void onApplicationEvent(BoardEvent event) {
        AbstractActivity activity = event.getActivity();
        userIncludedInBoardRepository.findAllByBoard(activity.getBoard())
                .forEach(userIncludedInTeam -> activityEventHandler.handleEvent(activity, userIncludedInTeam.getUser()));
    }
}
