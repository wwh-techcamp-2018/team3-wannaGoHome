package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.domain.SignUpDto;
import wannagohome.domain.User;
import wannagohome.repository.UserRepository;

import java.util.Optional;

@Service
public class UserService {
    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public User signIn() {
        return null;
    }

    public User signUp(SignUpDto dto) {
        log.debug("signUpDto: {}", dto);
        Optional<User> maybeUser = userRepository.findByEmail(dto.getEmail());
        if (maybeUser.isPresent())
            throw new RuntimeException();

        return userRepository.save(User.valueOf(dto, passwordEncoder));
    }
}
