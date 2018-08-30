package wannagohome.domain.team;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class TeamPermissionChangeDto {
    private Long teamId;
    private Long userId;
    private String permission;

}
