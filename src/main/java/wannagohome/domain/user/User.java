package wannagohome.domain.user;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import wannagohome.domain.error.ErrorType;
import wannagohome.exception.UnAuthenticationException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@ToString
@EqualsAndHashCode
public class User {
    public static User GUEST_USER = new GuestUser();
    public static final String DEFAULT_PROFILE = "https://s3.ap-northeast-2.amazonaws.com/wannagohome/default_image.png";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Email
    @NotBlank
    @Column(length = 40, unique = true, nullable = false, updatable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @NotBlank
    @Size(min = 1, max = 10)
    @Column(length = 10, nullable = false)
    private String name;

    @Column(nullable = false)
    private boolean deleted;

    private String profile;


    public String getProfile() {
        if(profile == null || profile.isEmpty())
            return DEFAULT_PROFILE;
        return this.profile;
    }

    public boolean isDefaultProfile() {
        return getProfile().equals(DEFAULT_PROFILE);
    }

    public void initializeProfile() {
        this.profile = "";
    }

    public static User valueOf(SignUpDto dto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    public void signIn(SignInDto dto, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(dto.getPassword(), password))
            throw new UnAuthenticationException(ErrorType.USER_PASSWORD, "비밀번호 를 확인해주세요.");
    }

    public boolean isGuestUser() {
        return false;
    }

    public String encodedCode(PasswordEncoder encoder) {
        return encoder.encode(email);
    }

    static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }

    @JsonIgnore
    public UserDto getUserDto() {
        UserDto userDto = new UserDto();
        userDto.setId(id);
        userDto.setName(name);
        return userDto;
    }
}
