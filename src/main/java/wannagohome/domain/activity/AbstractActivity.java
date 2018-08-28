package wannagohome.domain.activity;

import lombok.Getter;
import lombok.Setter;
import org.springframework.security.crypto.password.PasswordEncoder;
import wannagohome.domain.board.Board;
import wannagohome.domain.team.Team;
import wannagohome.domain.user.User;

import javax.persistence.*;
import java.util.Date;

@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "activityType")
public abstract class AbstractActivity implements Activity, Comparable<AbstractActivity>, Cloneable {

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

    @Column(updatable = false)
    @Getter
    protected Date registeredDate = new Date();

    @Getter
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ActivityType type;

    abstract public Board getBoard();

    abstract public Team getTeam();

    abstract public String getLink();

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

    public String getTopic(PasswordEncoder encoder) {
        return "/topic/user/" + receiver.encodedCode(encoder);
    }


    public void clearId() {
        id = null;
    }
}
