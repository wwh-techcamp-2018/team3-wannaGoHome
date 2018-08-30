package wannagohome.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import wannagohome.domain.team.Team;

import javax.persistence.*;
import java.util.Objects;

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

    public void changePermission(UserPermission permission) {
        this.permission = permission;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserIncludedInTeam that = (UserIncludedInTeam) o;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {

        return Objects.hash(id);
    }

    public boolean isAdmin() {
        return UserPermission.ADMIN == permission;
    }
}
