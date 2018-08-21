package wannagohome.domain;

import lombok.Builder;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Builder
@Entity
@DiscriminatorValue("BoardActivity")
public class BoardActivity extends AbstractActivity {

    @ManyToOne
    private Board board;

    @ManyToOne
    private Team team;

    @Override
    public Object[] getArguments() {
        return new Object[]{board.getTitle()};
    }
}
