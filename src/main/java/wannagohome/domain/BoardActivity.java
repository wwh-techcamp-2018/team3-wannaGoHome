package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Builder
@Entity
@DiscriminatorValue("BoardActivity")
@NoArgsConstructor
@AllArgsConstructor
public class BoardActivity extends AbstractActivity {

    @ManyToOne
    private Board board;

    @Override
    public Object[] getArguments() {
        return new Object[]{board.getTitle()};
    }
}
