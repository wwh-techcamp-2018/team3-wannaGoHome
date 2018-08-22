package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.AbstractActivity;

public class BoardEvent extends ApplicationEvent {

    @Getter
    private AbstractActivity activity;

    public BoardEvent(Object object, AbstractActivity activity) {
        super(object);
        this.activity = activity;
    }
}
