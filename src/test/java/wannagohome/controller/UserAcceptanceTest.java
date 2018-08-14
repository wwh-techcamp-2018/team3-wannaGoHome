package wannagohome.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import wannagohome.domain.SignInDto;
import wannagohome.domain.SignUpDto;
import wannagohome.domain.User;
import wannagohome.support.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class UserAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(UserAcceptanceTest.class);

    private static final String SIGNUP_URL = "/api/users";
    private static final String SIGNIN_URL = "/api/users/signin";

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Test
    public void signUp() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("aa@aa.com")
                .name("junsulime")
                .password("password1")
                .build();
        ResponseEntity<User> responseEntity = template().postForEntity(SIGNUP_URL, signUpDto, User.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        User user = responseEntity.getBody();
        log.debug("email: {}", user.getEmail());
        log.debug("name: {}", user.getName());
        log.debug("encoded password: {}", user.getPassword());

        assertThat(signUpDto.getName()).isEqualTo(user.getName());
        assertThat(signUpDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(passwordEncoder.matches(signUpDto.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    public void signUp_짧은_비밀번호() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("aa@aa.com")
                .name("junsulime")
                .password("short")
                .build();
        ResponseEntity<User> responseEntity = template().postForEntity(SIGNUP_URL, signUpDto, User.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        // TODO: 2018. 8. 14. BadRequest 시 에러메세지가 정해지면 이에 대해 테스트하자
    }

    @Test
    public void signIn() {
        SignInDto signInDto = new SignInDto("songintae@woowahan.com", "password1");
        ResponseEntity<User> responseEntity = template().postForEntity(SIGNIN_URL, signInDto, User.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        User user = responseEntity.getBody();
        assertThat(signInDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(passwordEncoder.matches(signInDto.getPassword(), user.getPassword())).isTrue();
    }

}
