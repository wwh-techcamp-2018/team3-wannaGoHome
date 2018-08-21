package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

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
}
