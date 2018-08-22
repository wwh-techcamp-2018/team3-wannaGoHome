package wannagohome.event;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;
import wannagohome.domain.ActivityType;
import wannagohome.domain.Card;
import wannagohome.domain.CardActivity;
import wannagohome.domain.User;

public class CardEvent extends ApplicationEvent {

    @Getter
    private CardActivity activity;

    public CardEvent(Object object, User source, Card card, ActivityType type, User target) {
        super(source);
        this.activity = CardActivity.valueOf(source, card, type, target);
    }
}
