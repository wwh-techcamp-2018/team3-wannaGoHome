package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.domain.SignInDto;
import wannagohome.domain.SignUpDto;
import wannagohome.domain.User;
import wannagohome.exception.UnauthenticationException;
import wannagohome.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User signIn(SignInDto dto) {
        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnauthenticationException("아이디 / 비밀번호 를 확인해주세요."));
        user.signIn(dto, passwordEncoder);
        return user;
    }

    public User signUp(SignUpDto dto) {
        Optional<User> maybeUser = userRepository.findByEmail(dto.getEmail());
        if (maybeUser.isPresent())
            throw new RuntimeException();

        return userRepository.save(User.valueOf(dto, passwordEncoder));
    }
}
