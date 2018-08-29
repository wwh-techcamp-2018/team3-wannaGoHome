package wannagohome.domain.user;

import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Getter
@Setter
@NoArgsConstructor
public class UserDto {
    private Long id;

    @Size(min = 1, max = 10)
    private String name;
    private String email;
    private String profile;
    private UserPermission userPermission;

    private UserDto(User user) {
        this.id = user.getId();
        this.name = user.getName();
        this.email = user.getEmail();
        this.profile = user.getProfile();

    }

    public static UserDto valueOf(User user) {
        return new UserDto(user);
    }

    public static UserDto userDtoWithPermission(UserIncludedInTeam includedInTeam) {
        UserDto userDto = new UserDto(includedInTeam.getUser());
        userDto.setUserPermission(includedInTeam.getPermission());
        return userDto;
    }

}
