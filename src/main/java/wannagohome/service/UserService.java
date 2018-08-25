package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.domain.ErrorType;
import wannagohome.domain.SignInDto;
import wannagohome.domain.SignUpDto;
import wannagohome.domain.User;
import wannagohome.exception.BadRequestException;
import wannagohome.exception.UnAuthenticationException;
import wannagohome.repository.UserRepository;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public User signIn(SignInDto dto) {
        User user = userRepository
                .findByEmail(dto.getEmail())
                .orElseThrow(() -> new UnAuthenticationException(ErrorType.USER_EMAIL, "아이디를 확인해주세요."));
        user.signIn(dto, passwordEncoder);
        return user;
    }

    public User signUp(SignUpDto dto) {
        if (userRepository.findByEmail(dto.getEmail()).isPresent()) {
            throw new BadRequestException(ErrorType.USER_EMAIL, "이미 존재하는 이메일 입니다.");
        }

        return userRepository.save(User.valueOf(dto, passwordEncoder));
    }
}
