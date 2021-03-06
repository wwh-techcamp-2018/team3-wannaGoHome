package wannagohome.domain.activity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.board.Board;
import wannagohome.domain.card.Card;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import java.util.Objects;

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

    private CardActivity(User source, Card card, ActivityType activityType, User target) {
        this.source = source;
        this.card = card;
        this.type = activityType;
        this.target = target;
    }

    public static CardActivity valueOf(User source, Card card, ActivityType activityType, User target) {
        return new CardActivity(source, card, activityType, target);
    }

    public static CardActivity valueOf(User source, Card card, ActivityType activityType) {
        return new CardActivity(source, card, activityType, null);
    }

    @Override
    public Object[] getArguments() {
        return new Object[]{
                source.getName(),
                getTeam().getName(),
                getBoard().getTitle(),
                card.getTitle(),
                Objects.isNull(target) ? "" : target.getName()
        };
    }

    @Override
    public Board getBoard() {
        return card.getTask().getBoard();
    }

    @Override
    public Team getTeam() {
        return card.getTeam();
    }

    public String getLink() {
        return "/board/" + getBoard().getId();
    }

}
