package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import wannagohome.domain.AbstractActivity;
import wannagohome.repository.UserIncludedInTeamRepository;

@Component
public class TeamEventListener implements ApplicationListener<TeamEvent> {

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private ActivityEventHandler activityEventHandler;

    @Override
    public void onApplicationEvent(TeamEvent event) {
        AbstractActivity activity = event.getActivity();
        userIncludedInTeamRepository.findAllByTeam(activity.getTeam())
                .forEach(userIncludedInTeam -> activityEventHandler.handleEvent(activity, userIncludedInTeam.getUser()));
    }
}
