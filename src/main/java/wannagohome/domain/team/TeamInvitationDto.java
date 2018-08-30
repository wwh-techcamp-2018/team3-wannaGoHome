package wannagohome.domain.team;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class TeamInvitationDto {
    private Long id;
    private String isAgree;

    public boolean getIsAgree() {
        return Boolean.valueOf(this.isAgree);
    }
}
