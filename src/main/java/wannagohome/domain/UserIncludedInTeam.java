package wannagohome.domain;

import lombok.Builder;
import lombok.Getter;

import javax.persistence.*;

@Entity
@Builder
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

    @Enumerated(EnumType.ORDINAL)
    private UserPermission permission;

}
