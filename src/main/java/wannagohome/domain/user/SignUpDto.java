package wannagohome.domain.user;

import lombok.*;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString
public class SignUpDto {

    @Email
    @NotBlank
    private String email;

    @NotBlank
    @Size(min = 8, max = 16)
    @Pattern(regexp = "^[0-9a-zA-Z]+")
    private String password;

    @NotBlank
    @Size(min = 1, max = 10)
    private String name;
}
