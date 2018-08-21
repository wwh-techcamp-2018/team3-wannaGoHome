package wannagohome.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;


@Entity
@DiscriminatorValue("TeamActivity")
public class TeamActivity extends AbstractActivity {

    @ManyToOne
    private Team team;

    @Override
    public Object[] getArguments() {
        return new Object[]{this.team.getName()};
    }
}
