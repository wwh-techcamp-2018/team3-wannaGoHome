package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.*;

public class BoardEvent extends ApplicationEvent {

    @Getter
    private BoardActivity activity;

    public BoardEvent(Object object, User source, Board board, ActivityType activityType) {
        super(object);
        this.activity = BoardActivity.valueOf(source, board, activityType);
    }
}
