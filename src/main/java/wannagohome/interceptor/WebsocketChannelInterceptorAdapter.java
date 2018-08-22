package wannagohome.interceptor;

import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.util.StringUtils;
import wannagohome.domain.WebsocketUser;

public class WebsocketChannelInterceptorAdapter implements ChannelInterceptor {

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {

        StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

        if (StompCommand.CONNECT.equals(accessor.getCommand())) {
            String user = accessor.getFirstNativeHeader("user");
            if (!StringUtils.isEmpty(user)) {
                accessor.setUser(new WebsocketUser(user));
            }
        }
        return message;
    }
}
