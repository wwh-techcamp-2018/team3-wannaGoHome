package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;
import wannagohome.domain.activity.AbstractActivity;

@Component
public class PersonalEventListener implements ApplicationListener<PersonalEvent> {

    @Autowired
    private ActivityEventHandler activityEventHandler;

    @Override
    public void onApplicationEvent(PersonalEvent event) {
        AbstractActivity activity = event.getActivity();
        activityEventHandler.handleEvent(activity, activity.getReceiver());
    }
}
