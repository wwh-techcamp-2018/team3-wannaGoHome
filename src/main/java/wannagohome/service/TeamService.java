package wannagohome.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import wannagohome.domain.board.BoardOfTeamDto;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.team.Team;
import wannagohome.domain.team.TeamPermissionChangeDto;
import wannagohome.domain.user.User;
import wannagohome.domain.user.UserDto;
import wannagohome.domain.user.UserIncludedInTeam;
import wannagohome.domain.user.UserPermission;
import wannagohome.exception.DuplicationException;
import wannagohome.exception.NotFoundException;
import wannagohome.exception.UnAuthorizedException;
import wannagohome.repository.TeamRepository;
import wannagohome.repository.UserIncludedInTeamRepository;
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
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

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
        if (teamRepository.findByName(team.getName()).isPresent()) {
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
        userIncludedInTeamRepository.findAllByUser(user)
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
        return userIncludedInTeamRepository.findAllByUser(user)
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

    public UserIncludedInTeam changePermission(TeamPermissionChangeDto permissionDto) {
        User user = userService.findByUserId(permissionDto.getUserId());
        Team team = findTeamById(permissionDto.getTeamId());
        UserIncludedInTeam userIncludedInTeam = userIncludedInTeamRepository.findByUserAndTeam(user,team).get();
        userIncludedInTeam.changePermission(UserPermission.of(permissionDto.getPermission()));
        return userIncludedInTeamRepository.save(userIncludedInTeam);
    }
}
