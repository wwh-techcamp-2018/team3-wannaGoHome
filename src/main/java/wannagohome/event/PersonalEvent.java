package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.activity.AbstractActivity;

public class PersonalEvent extends ApplicationEvent {
    @Getter
    private AbstractActivity activity;

    public PersonalEvent(Object object, AbstractActivity activity) {
        super(object);
        this.activity = activity;
    }
}
