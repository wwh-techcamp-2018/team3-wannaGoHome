package wannagohome.component;

import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.stereotype.Component;
import wannagohome.domain.AbstractActivity;
import wannagohome.domain.User;

import javax.annotation.Resource;

@Component
public class ActivityMessageGenerator {

//    @Resource(name = "activityMessageSourceAccessor")
//    private MessageSourceAccessor messageSourceAccessor;
//
//    public String buildActivityMessage(AbstractActivity abstractActivity) {
//        return buildActivityMessage(abstractActivity, null);
//    }
//
//    public String buildActivityMessage(AbstractActivity abstractActivity, User target) {
//        return abstractActivity.buildMessage(messageSourceAccessor, target);
//    }
}
