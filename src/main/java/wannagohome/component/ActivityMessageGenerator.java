package wannagohome.component;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.Activity;

import javax.annotation.Resource;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class ActivityMessageGenerator {

    @Resource(name = "activityMessageSourceAccessor")
    private MessageSourceAccessor messageSourceAccessor;

    public List<String> generateMessages(List<? extends AbstractActivity> activities) {
        return activities.stream()
                .map(this::generateMessage)
                .collect(Collectors.toList());
    }

    public String generateMessage(Activity activity) {
        return messageSourceAccessor.getMessage(activity.getCode(), activity.getArguments());
    }
}
