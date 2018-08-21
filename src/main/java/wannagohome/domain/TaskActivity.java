package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;

@Entity
@DiscriminatorValue("TaskActivity")
@NoArgsConstructor
@AllArgsConstructor
public class TaskActivity extends AbstractActivity {

    @ManyToOne
    private Task task;

    @Override
    public Object[] getArguments() {
        return new Object[]{this.task.getTitle()};
    }
}
