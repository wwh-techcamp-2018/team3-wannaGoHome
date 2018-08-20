package wannagohome.domain;

import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;

import javax.persistence.*;
import java.util.Date;


@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn
public abstract class AbstractActivity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    protected User user;

    @CreatedDate
    @Column(updatable = false)
    protected Date registeredDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    protected ActivityType type;

    protected abstract Object[] getArguments();
}
