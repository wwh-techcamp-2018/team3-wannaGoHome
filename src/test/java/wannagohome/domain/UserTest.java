package wannagohome.domain;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import wannagohome.domain.user.SignInDto;
import wannagohome.domain.user.User;
import wannagohome.exception.UnAuthenticationException;

import static org.assertj.core.api.Assertions.assertThat;

public class UserTest {
    private static final Logger log = LoggerFactory.getLogger(UserTest.class);

    private User user;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    @Before
    public void setUp() throws Exception {
        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .build();
        log.debug("encoded password: {}", user.getPassword());
    }

    @Test
    public void signIn() {
        user.signIn(new SignInDto("jhyang@good.looking", "password1"), passwordEncoder);
    }

    @Test(expected = UnAuthenticationException.class)
    public void signIn_잘못된_비밀번호() {
        user.signIn(new SignInDto("jhyang@good.looking", "wrongpassword"), passwordEncoder);
    }

    @Test
    public void getProfile() {
        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .build();
        assertThat(user.getProfile()).isEqualTo(User.DEFAULT_PROFILE);
    }

    @Test
    public void initializeProfile() {
        user = User.builder()
                .email("jhyang@good.looking")
                .name("jhyang")
                .password(passwordEncoder.encode("password1"))
                .profile("profileImage")
                .build();
        assertThat(user.getProfile()).isEqualTo("profileImage");

        user.initializeProfile();
        assertThat(user.getProfile()).isEqualTo(User.DEFAULT_PROFILE);
    }
}