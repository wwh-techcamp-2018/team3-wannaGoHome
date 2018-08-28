package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.user.SignInDto;
import wannagohome.domain.user.SignUpDto;
import wannagohome.domain.user.User;
import wannagohome.exception.BadRequestException;
import wannagohome.exception.UnAuthenticationException;
import wannagohome.repository.UserRepository;
import wannagohome.service.file.UploadService;

import javax.annotation.Resource;
import javax.transaction.Transactional;

@Service
public class UserService {

    private static final Logger log = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "imageUploadService")
    private UploadService uploadService;


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

    @Transactional
    public User initializeProfile(User user) {
        if(!user.isDefaultProfile()) {
            uploadService.fileDelete(user.getProfile());
        }
        user.initializeProfile();
        return userRepository.save(user);
    }

    @Transactional
    public User changeProfile(User user, MultipartFile file) {
        if(!user.isDefaultProfile()) {
            uploadService.fileDelete(user.getProfile());
        }
        user.setProfile(uploadService.fileUpload(file));
        return save(user);
    }

    public User save(User user) {
        return userRepository.save(user);
    }


}
