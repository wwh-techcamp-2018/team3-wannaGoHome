package wannagohome.domain;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activityType")
public abstract class AbstractActivity implements Activity, Comparable<AbstractActivity>, Cloneable {

    @Setter
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Setter
    @Getter
    @ManyToOne
    protected User receiver;

    @Getter
    @ManyToOne
    protected User source;

    @CreatedDate
    @Column(updatable = false)
    protected Date registeredDate;

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ActivityType type;

    abstract public Board getBoard();

    abstract public Team getTeam();

    @Column(nullable = false, insertable = false, updatable = false)
    private String activityType;

    @Override
    public int compareTo(AbstractActivity o) {
        return registeredDate.compareTo(o.registeredDate);
    }

    public String getCode() {
        return type.getCode();
    }

    public Object clone() {
        Object objReturn;
        try {
            objReturn = super.clone();
            return objReturn;
        } catch (CloneNotSupportedException e) {
            throw new RuntimeException();
        }

    }


}
