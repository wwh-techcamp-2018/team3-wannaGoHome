package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.TeamActivity;
import wannagohome.domain.board.BoardOfTeamDto;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.team.RemoveUserFromTeamDto;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamPermissionChangeDto;
import wannagohome.domain.user.*;
import wannagohome.event.PersonalEvent;
import wannagohome.event.TeamEvent;
import wannagohome.exception.DuplicationException;
import wannagohome.exception.NotFoundException;
import wannagohome.exception.UnAuthorizedException;
import wannagohome.repository.*;
import wannagohome.service.file.UploadService;

import javax.annotation.Resource;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeamService {

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Autowired
    private SimpMessageSendingOperations simpMessageSendingOperations;

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder encoder;

    @Autowired
    private UserService userService;

    @Resource(name = "imageUploadService")
    private UploadService uploadService;
    
    private static final Logger log = LoggerFactory.getLogger(TeamService.class);

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "boardSummary", key = "#user.id"),
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "teamsByUser", key = "#user.id"),
                    @CacheEvict(value = "createBoardInfo", allEntries = true)
            }
    )
    public BoardOfTeamDto create(Team team, User user) {
        if (teamRepository.findByNameAndDeletedFalse(team.getName()).isPresent()) {
            throw new DuplicationException(ErrorType.TEAM_NAME, "이미 같은 이름의 팀이 존재합니다.");
        }
        Team newTeam = teamRepository.save(team);
        userIncludedInTeamRepository.save(createRelation(user, newTeam, UserPermission.ADMIN));
        return new BoardOfTeamDto(newTeam);
    }

    private UserIncludedInTeam createRelation(User user, Team team, UserPermission userPermission) {
        return new UserIncludedInTeam(user, team, userPermission);
    }

    @Cacheable(value = "teamById", key = "#id")
    public Team findTeamById(Long id) {
        return teamRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ErrorType.TEAM_NAME, "Team이 존재하지 않습니다."));
    }

    @Cacheable(value = "teamsByUser", key = "#user.id")
    public List<Team> findTeamsByUser(User user) {
        List<Team> teams = new ArrayList<>();
        userIncludedInTeamRepository.findAllByUserAndTeamDeletedFalse(user)
                .stream()
                .forEach(userIncludedInTeam -> teams.add(userIncludedInTeam.getTeam()));
        return teams;
    }

    public List<Team> findAll() {
        return teamRepository.findAll();
    }

    public Team viewTeam(Long id, User user) {
        Team team = teamRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new NotFoundException(ErrorType.TEAM_NAME, "Team이 존재하지 않습니다."));
        confirmAuthorityOfUser(user, team);
        return team;
    }

    public List<UserDto> findByTeam(Team team) {
        return userIncludedInTeamRepository.findAllByTeam(team)
                .stream().map(userIncludedInTeam -> UserDto.userDtoWithPermission(userIncludedInTeam))
                .collect(Collectors.toList());
    }

    public UserDto findByUserOfTeam(User user, Team team) {
        return UserDto.userDtoWithPermission(userIncludedInTeamRepository.findByUserAndTeam(user,team).get());
    }

    public List<Team> findByUser(User user) {
        return userIncludedInTeamRepository.findAllByUserAndTeamDeletedFalse(user)
                .stream().map(UserIncludedInTeam::getTeam).collect(Collectors.toList());
    }

    public List<UserDto> findUsersByKeyword(Long teamId, String keyword) {
        Team team = findTeamById(teamId);
        List<User> users = userIncludedInTeamRepository.findAllByTeam(team)
                .stream()
                .map(userIncludedInTeam -> userIncludedInTeam.getUser())
                .collect(Collectors.toList());
        return userService.findAllByIdNotIn(users, keyword)
                .stream().map(user -> UserDto.valueOf(user)).collect(Collectors.toList());
    }

    private UserIncludedInTeam confirmAuthorityOfUser(User user, Team team) {
        return userIncludedInTeamRepository.findByUserAndTeam(user,team)
                .orElseThrow(() -> new UnAuthorizedException(ErrorType.UNAUTHORIZED, "Team에 접근할 권한이 없습니다."));
    }

    @Transactional
    public Team changeProfile(Team team, MultipartFile file) {
        if(!team.isDefaultProfile()) {
            uploadService.fileDelete(team.getProfile());
        }
        team.setProfile(uploadService.fileUpload(file));
        return teamRepository.save(team);
    }

    @Caching(
            evict = {
                    @CacheEvict(value = "boardSummary", key = "#user.id"),
                    @CacheEvict(value = "recentlyViewBoard", key = "#user.id"),
                    @CacheEvict(value = "teamsByUser", key = "#user.id"),
                    @CacheEvict(value = "createBoardInfo", allEntries = true)
            }
    )
    public UserIncludedInTeam includeInTeam(User user, Team team) {
        UserIncludedInTeam userIncludedInTeam = createRelation(user, team, UserPermission.MEMBER);
        return userIncludedInTeamRepository.save(userIncludedInTeam);
    }


    @CacheEvict(value = "teamById", key = "#permissionDto.teamId")
    public UserIncludedInTeam changePermission(User source, TeamPermissionChangeDto permissionDto) {
        User user = userService.findByUserId(permissionDto.getUserId());
        Team team = findTeamById(permissionDto.getTeamId());
        UserIncludedInTeam userIncludedInTeam = userIncludedInTeamRepository.findByUserAndTeam(user,team).get();
        userIncludedInTeam.changePermission(UserPermission.of(permissionDto.getPermission()));
        userIncludedInTeam = userIncludedInTeamRepository.save(userIncludedInTeam);

        TeamActivity activity = TeamActivity.valueOf(source, team, ActivityType.TEAM_AUTHORITY, user, UserPermission.of(permissionDto.getPermission()));
        activity.setReceiver(user);
        applicationEventPublisher.publishEvent(new PersonalEvent(this, activity));
        return userIncludedInTeam;
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "boardSummary", key = "#removeUserFromTeamDto.userId"),
                    @CacheEvict(value = "recentlyViewBoard", key = "#removeUserFromTeamDto.userId"),
                    @CacheEvict(value = "teamsByUser", key = "#removeUserFromTeamDto.userId"),
                    @CacheEvict(value = "createBoardInfo", allEntries = true)
            }
    )
    public UserDto removeUserFromTeam(User user, RemoveUserFromTeamDto removeUserFromTeamDto) {
        Team team = findTeamById(removeUserFromTeamDto.getTeamId());
        User target = userService.findByUserId(removeUserFromTeamDto.getUserId());
        userIncludedInTeamRepository
                .findByUserAndTeam(user, team)
                .filter(userIncludedInTeam -> userIncludedInTeam.isAdmin())
                .orElseThrow(() -> new UnAuthorizedException(ErrorType.UNAUTHORIZED, "유저에 권한이 없습니다."));

        UserIncludedInTeam targetIncludeInTeam
                = userIncludedInTeamRepository.findByUserAndTeam(target, team)
                .orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "팀에 해당하는 유저기 없습니다."));
        userIncludedInTeamRepository.delete(targetIncludeInTeam);
        List<UserIncludedInBoard> userIncludedInBoards = userIncludedInBoardRepository
                .findByBoardTeamAndUser(targetIncludeInTeam.getTeam(), targetIncludeInTeam.getUser());

        String userCode = target.encodedCode(encoder);
        userIncludedInBoardRepository.deleteAll(userIncludedInBoards);

        userIncludedInBoards.forEach((userIncludedInBoard -> {
            simpMessageSendingOperations.convertAndSend(
                    String.format("/topic/boards/%d/%s", userIncludedInBoard.getBoard().getId(), userCode),
                    ""
            );
        }));

        TeamActivity teamActivity = TeamActivity.valueOf(user,team,ActivityType.TEAM_MEMBER_REMOVE, target);
        teamActivity.setReceiver(target);
        applicationEventPublisher.publishEvent(new PersonalEvent(this, teamActivity));
        return UserDto.valueOf(targetIncludeInTeam.getUser());
    }

    @Transactional
    @Caching(
            evict = {
                    @CacheEvict(value = "boardSummary", allEntries = true),
                    @CacheEvict(value = "recentlyViewBoard", allEntries = true),
                    @CacheEvict(value = "teamsByUser", allEntries = true),
                    @CacheEvict(value = "createBoardInfo", allEntries = true)
            }
    )
    public Team deleteTeam(User user, Long teamId) {
        Team team = findTeamById(teamId);
        userIncludedInTeamRepository.findByUserAndTeam(user, team)
                .filter(UserIncludedInTeam::isAdmin)
                .orElseThrow(() -> new UnAuthorizedException(ErrorType.UNAUTHORIZED, "팀을 지울 권한일 없습니다."));

        team.delete();
        boardRepository.findAllByTeamAndDeletedFalse(team).forEach(board -> {
            board.delete();
            boardRepository.save(board);
        });
        teamRepository.save(team);

        TeamActivity activity = TeamActivity.valueOf(user, team, ActivityType.TEAM_DELETE);
        applicationEventPublisher.publishEvent(new TeamEvent(this, activity));
        return team;
    }
}
