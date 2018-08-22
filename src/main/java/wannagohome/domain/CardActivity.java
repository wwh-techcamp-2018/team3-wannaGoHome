package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
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

    @Builder
    public CardActivity(User source, Card card, ActivityType activityType, User target) {
        this.source = source;
        this.card = card;
        this.type = activityType;
        this.target = target;
    }

    @Override
    public Board getBoard() {
        return card.getTask().getBoard();
    }

    @Override
    public Team getTeam() {
        return card.getTask().getBoard().getTeam();
    }

}
