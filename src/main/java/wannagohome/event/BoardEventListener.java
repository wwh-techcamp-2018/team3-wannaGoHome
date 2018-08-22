package wannagohome.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.BoardActivity;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class BoardEventListener implements ApplicationListener<BoardEvent> {

    private static final Logger log = LoggerFactory.getLogger(BoardEventListener.class);

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
