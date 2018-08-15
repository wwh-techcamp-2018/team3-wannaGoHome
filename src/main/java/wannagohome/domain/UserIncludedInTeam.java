package wannagohome.domain;

import lombok.Getter;

import javax.persistence.*;

@Entity
@Getter
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

    public UserIncludedInTeam() {
    }

    public UserIncludedInTeam(User user, Team team) {
        this.user = user;
        this.team = team;
        this.permission = UserPermission.ADMIN;
    }
}
