package wannagohome.domain.user;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Size;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserDto userDto = (UserDto) o;
        return Objects.equals(id, userDto.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
