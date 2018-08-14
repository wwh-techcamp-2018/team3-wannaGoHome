package wannagohome.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class SignInDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^[0-9a-zA-Z]+")
    private String password;
}
