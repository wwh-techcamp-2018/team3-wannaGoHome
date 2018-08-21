package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activityType")
public abstract class AbstractActivity implements Activity, Comparable<AbstractActivity> {

    public static final String TEAM_ACTIVITY = "TeamActivity";
    public static final String BOARD_ACTIVITY = "BoardActivity";
    public static final String TASK_ACTIVITY = "TaskActivity";
    public static final String CARD_ACTIVITY = "CardActivity";

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Setter
    @ManyToOne
    protected User receiver;

    @ManyToOne
    protected User source;

    @CreatedDate
    @Column(updatable = false)
    protected Date registeredDate;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ActivityType type;


    @Column(nullable = false, insertable = false, updatable = false)
    private String activityType;

    @Override
    public int compareTo(AbstractActivity o) {
        return registeredDate.compareTo(o.registeredDate);
    }

    public String getCode() {
        return type.getCode();
    }

}
