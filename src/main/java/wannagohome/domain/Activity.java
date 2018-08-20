package wannagohome.domain;

import javax.persistence.*;
import java.util.Date;


@Entity
public class Activity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @ManyToOne
    @JoinColumn(name = "BOARD_ID")
    private Board board;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(updatable = false)
    private Date registeredDate;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ActivityType type;


}
