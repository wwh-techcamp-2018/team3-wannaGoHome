package wannagohome.controller;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import wannagohome.domain.error.ErrorEntity;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.user.*;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import java.util.Arrays;
import java.util.List;

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

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        User user = responseEntity.getBody();
        log.debug("email: {}", user.getEmail());
        log.debug("name: {}", user.getName());
        log.debug("encoded password: {}", user.getPassword());

        assertThat(signUpDto.getName()).isEqualTo(user.getName());
        assertThat(signUpDto.getEmail()).isEqualTo(user.getEmail());
        assertThat(passwordEncoder.matches(signUpDto.getPassword(), user.getPassword())).isTrue();
    }

    @Test
    public void signUp_이미_존재하는_아이디() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("kimyeon@woowahan.com")
                .name("kimyeon")
                .password("password1")
                .build();
        ResponseEntity<List> responseEntity = template().postForEntity(SIGNUP_URL, signUpDto, List.class);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }

    @Test
    public void signUp_짧은_비밀번호() {
        SignUpDto signUpDto = SignUpDto.builder()
                .email("aa@aa.com")
                .name("junsulime")
                .password("short")
                .build();
        ResponseEntity<ErrorEntity[]> responseEntity = template().postForEntity(SIGNUP_URL, signUpDto, ErrorEntity[].class);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);

        ErrorEntity errorEntity = responseEntity.getBody()[0];
        log.debug("error type: {}", errorEntity.getErrorType());
        log.debug("error message: {}", errorEntity.getMessage());
        Arrays.stream(responseEntity.getBody())
                .filter(error-> error.sameErrorType(ErrorType.USER_PASSWORD))
                .findFirst()
                .orElseThrow(RuntimeException::new);
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

    @Test
    public void profile() {
        SignInDto signInDto = new SignInDto("songintae@woowahan.com", "password1");
        RequestEntity requestEntity= new RequestEntity.Builder()
                .withUrl("/api/users/profile")
                .withMethod(HttpMethod.GET)
                .withReturnType(MyPageDto.class)
                .build();

        ResponseEntity<MyPageDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getTeams().size()).isEqualTo(1);
        assertThat(responseEntity.getBody().getActivities().size()).isEqualTo(0);
        assertThat(responseEntity.getBody().getUser().getEmail()).isEqualTo(signInDto.getEmail());
    }

    @Test
    public void initializeProfile() {
        SignInDto signInDto = new SignInDto("songintae@woowahan.com", "password1");
        RequestEntity requestEntity= new RequestEntity.Builder()
                .withUrl("/api/users/profile/init")
                .withMethod(HttpMethod.POST)
                .withReturnType(UserDto.class)
                .build();

        ResponseEntity<UserDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getProfile()).isEqualTo(User.DEFAULT_PROFILE);
    }

    @Test
    public void changeName() {
        SignInDto signInDto = new SignInDto("songintae@woowahan.com", "password1");
        UserDto userDto = UserDto.valueOf(
                User.builder()
                        .name("kookooku")
                        .build()
        );

        RequestEntity requestEntity= new RequestEntity.Builder()
                .withUrl("/api/users/profile")
                .withMethod(HttpMethod.PUT)
                .withBody(userDto)
                .withReturnType(UserDto.class)
                .build();

        ResponseEntity<UserDto> responseEntity = basicAuthRequest(requestEntity, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getName()).isEqualTo("kookooku");
    }


}
