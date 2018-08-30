package wannagohome.domain.user;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    @Pattern(regexp = "^[_0-9a-zA-Z-]+@[0-9a-zA-Z]+(.[0-9a-zA-Z-]+)$")
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^[0-9a-zA-Z]+")
    private String password;
}
