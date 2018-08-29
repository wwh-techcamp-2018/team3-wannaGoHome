package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.TeamActivity;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.team.TeamInvitationDto;
import wannagohome.domain.team.TeamInvite;
import wannagohome.domain.user.SignInDto;
import wannagohome.domain.user.SignUpDto;
import wannagohome.domain.user.User;
import wannagohome.event.TeamEvent;
import wannagohome.exception.BadRequestException;
import wannagohome.exception.UnAuthenticationException;
import wannagohome.repository.UserRepository;
import wannagohome.service.file.UploadService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Resource(name = "imageUploadService")
    private UploadService uploadService;

    @Autowired
    private TeamInviteService teamInviteService;

    @Autowired
    private TeamService teamService;

    @Autowired
    ApplicationEventPublisher publisher;


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

    public TeamInvite processTeamInvitation(User user, TeamInvitationDto invitationDto) {
        TeamInvite teamInvite = teamInviteService.findById(invitationDto.getId());
        if(invitationDto.getIsAgree()) {
            teamService.includeInTeam(teamInvite.getMember(), teamInvite.getTeam());
            TeamActivity teamActivity  = TeamActivity.valueOf(user, teamInvite.getTeam(), ActivityType.TEAM_MEMBER_ADD);
            publisher.publishEvent(new TeamEvent(this, teamActivity));
        }
        teamInviteService.deleteById(invitationDto.getId());
        return teamInvite;
    }

    public List<User> findAllByIdNotIn(List<User> users, String keyword) {
        List<Long> userIds = users.stream().map(user -> user.getId()).collect(Collectors.toList());
        Set<User> userEmailSet = new HashSet<User>(userRepository.findAllByIdNotInAndEmailContainingIgnoreCase(userIds, keyword));
        Set<User> userNameSet = new HashSet<User>(userRepository.findAllByIdNotInAndNameContainingIgnoreCase(userIds, keyword));
        userEmailSet.addAll(userNameSet);
        return new ArrayList<User>(userEmailSet);
    }

}
