package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import wannagohome.domain.ActivityInitDto;
import wannagohome.domain.User;
import wannagohome.repository.UserIncludedInBoardRepository;
import wannagohome.repository.UserIncludedInTeamRepository;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class NotificationService {

    @Autowired
    private ActivityService activityService;

    @Autowired
    private UserIncludedInTeamRepository userIncludedInTeamRepository;

    @Autowired
    private UserIncludedInBoardRepository userIncludedInBoardRepository;

    @Resource(name = "biDirectionEncoder")
    private PasswordEncoder biDirectionDecoder;

    public ActivityInitDto initNotification(User user) {
        List<String> messages = activityService.findUserActivities(user);
        List<String> topics = generateTopics(user);

        return new ActivityInitDto(topics, messages, biDirectionDecoder.encode(user.getEmail()));
    }

    @Cacheable(value = "generateTopics", key = "#user.id")
    public List<String> generateTopics(User user) {
        List<String> topics = new ArrayList<>();
        topics.addAll(userIncludedInTeamRepository.findAllByUser(user).stream().map(userIncludedInTeam -> "/topic/activity/team/" + userIncludedInTeam.getTeam().getId()).collect(Collectors.toList()));
        topics.addAll(userIncludedInBoardRepository.findAllByUser(user).stream().map(userIncludedInBoard -> "/topic/activity/board/" + userIncludedInBoard.getBoard().getId()).collect(Collectors.toList()));

        return topics;
    }
}
