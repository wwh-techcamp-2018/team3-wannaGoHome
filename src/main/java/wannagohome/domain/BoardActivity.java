package wannagohome.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("BoardActivity")
public class BoardActivity extends AbstractActivity {

    @ManyToOne
    private Board board;

    @Override
    protected Object[] getArguments() {
        return new Object[]{board.getTitle()};
    }
}
