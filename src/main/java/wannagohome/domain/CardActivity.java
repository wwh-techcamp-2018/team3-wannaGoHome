package wannagohome.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("CardActivity")
public class CardActivity extends AbstractActivity {

    @Override
    public Object[] getArguments() {
        return new Object[0];
    }
}
