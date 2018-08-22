package wannagohome.event;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessageSendingOperations;

public class ActivityEventListener {

    @Autowired
    protected SimpMessageSendingOperations simpMessageSendingOperations;
}
