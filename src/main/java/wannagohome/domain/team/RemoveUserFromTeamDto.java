package wannagohome.domain.team;

import lombok.Getter;
import lombok.Setter;
import javax.validation.constraints.NotNull;

@Getter
@Setter
public class RemoveUserFromTeamDto {

    @NotNull
    private Long userId;
    @NotNull
    private Long teamId;

}
