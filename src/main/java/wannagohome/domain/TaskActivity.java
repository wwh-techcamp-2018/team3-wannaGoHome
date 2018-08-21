package wannagohome.domain;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TaskActivity")
public class TaskActivity extends AbstractActivity {

    @ManyToOne
    private Task task;

    @Override
    protected Object[] getArguments() {
        return new Object[]{this.task.getTitle()};
    }
}
