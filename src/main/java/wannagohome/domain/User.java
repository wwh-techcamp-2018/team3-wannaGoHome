package wannagohome.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import wannagohome.exception.UnAuthenticationException;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {
    public static User GUEST_USER = new GuestUser();

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

    @OneToMany(mappedBy = "user")
    private List<UserIncludedInTeam> includedTeams;

    @OneToMany(mappedBy = "user")
    private List<UserIncludedInBoard> includedBoards;

    @Column(nullable = false)
    private boolean deleted;

    public static User valueOf(SignUpDto dto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }

    public void signIn(SignInDto dto, PasswordEncoder passwordEncoder) {
        if (!passwordEncoder.matches(dto.getPassword(), password))
            throw new UnAuthenticationException("아이디 / 비밀번호 를 확인해주세요.");
    }

    public boolean isGuestUser() {
        return false;
    }

    static class GuestUser extends User {
        @Override
        public boolean isGuestUser() {
            return true;
        }
    }
}
