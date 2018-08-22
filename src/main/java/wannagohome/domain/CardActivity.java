package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Getter
@Entity
@DiscriminatorValue("CardActivity")
@NoArgsConstructor
@AllArgsConstructor
public class CardActivity extends AbstractActivity {

    @ManyToOne
    private Card card;

    @ManyToOne
    private User target;


    @Override
    public Object[] getArguments() {
        return new Object[]{
                card.getTitle(),
                target.getName()
        };
    }

    public static CardActivity valueOf(User user, Card card, ActivityType type, User target) {
        CardActivity cardActivity = new CardActivity();
        cardActivity.source = user;
        cardActivity.card = card;
        cardActivity.type = type;
        cardActivity.target = target;
        return cardActivity;
    }

    public Board getBoard() {
        return card.getTask().getBoard();
    }

    @Override
    public String getTopicUrl() {
        return "/topic/activity/board/" + getBoard().getId();
    }
}
