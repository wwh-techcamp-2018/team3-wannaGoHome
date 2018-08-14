package wannagohome.domain;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
public class User {

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

//    private List<Team> includedTeams;

    @Column(nullable = false)
    private boolean deleted;

    public static User valueOf(SignUpDto dto, PasswordEncoder passwordEncoder) {
        return User.builder()
                .email(dto.getEmail())
                .name(dto.getName())
                .password(passwordEncoder.encode(dto.getPassword()))
                .build();
    }
}
