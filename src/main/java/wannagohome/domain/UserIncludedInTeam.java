package wannagohome.domain;

import javax.persistence.*;

@Entity
public class UserIncludedInTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.ORDINAL)
    private UserPermission permission;

}
