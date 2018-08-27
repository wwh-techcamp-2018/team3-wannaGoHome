package wannagohome.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.team.Team;

import javax.persistence.*;

@Entity
@Builder
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class UserIncludedInTeam {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "USER_ID")
    private User user;

    @Getter
    @ManyToOne
    @JoinColumn(name = "TEAM_ID")
    private Team team;

    @Enumerated(EnumType.STRING)
    private UserPermission permission;

    public UserIncludedInTeam(User user, Team team, UserPermission userPermission) {
        this.user = user;
        this.team = team;
        this.permission = userPermission;
    }
}
