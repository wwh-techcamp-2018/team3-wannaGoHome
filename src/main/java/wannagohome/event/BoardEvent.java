package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.ActivityType;
import wannagohome.domain.Board;
import wannagohome.domain.BoardActivity;
import wannagohome.domain.User;

public class BoardEvent extends ApplicationEvent {

    @Getter
    private BoardActivity activity;

    public BoardEvent(Object object, User source, Board board, ActivityType activityType) {
        super(object);
        this.activity = BoardActivity.valueOf(source, board, activityType);
    }
}
