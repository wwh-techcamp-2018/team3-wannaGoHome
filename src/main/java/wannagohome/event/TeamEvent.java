package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.AbstractActivity;

public class TeamEvent extends ApplicationEvent {

    @Getter
    private AbstractActivity activity;

    public TeamEvent(Object object, AbstractActivity activity) {
        super(object);
        this.activity = activity;
    }

}
