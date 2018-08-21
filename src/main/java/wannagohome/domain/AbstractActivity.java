package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activityType")
public abstract class AbstractActivity implements Comparable<AbstractActivity> {

    public static final String TEAM_ACTIVITY = "BoardActivity";
    public static final String BOARD_ACTIVITY = "BoardActivity";
    public static final String TASK_ACTIVITY = "TaskActivity";
    public static final String CARD_ACTIVITY = "CardActivity";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private User receiver;

    @ManyToOne
    private User source;

    @CreatedDate
    @Column(updatable = false)
    protected Date registeredDate;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ActivityType type;


    @Column(nullable = false, insertable = false, updatable = false)
    private String activityType;

    public abstract Object[] getArguments();

    @Override
    public int compareTo(AbstractActivity o) {
        return registeredDate.compareTo(o.registeredDate);
    }
}
