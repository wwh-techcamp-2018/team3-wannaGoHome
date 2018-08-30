package wannagohome.domain.team;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TeamPermissionChangeDto {
    private Long teamId;
    private Long userId;
    private String permission;

}
