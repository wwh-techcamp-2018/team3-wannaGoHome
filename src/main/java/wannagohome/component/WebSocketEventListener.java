package wannagohome.component;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

@Component
public class WebSocketEventListener {
    private static final Logger log = LoggerFactory.getLogger(WebSocketEventListener.class);

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        System.out.println("Session connected!");
        System.out.println(event.getMessage().getHeaders().keySet());
        System.out.println(event.getMessage().getHeaders().get("simpSessionId"));
        System.out.println(event.getMessage().getHeaders().get("simpMessageType"));

        MessageHeaders headers = event.getMessage().getHeaders();
        for (Object o : headers.keySet()) {
            log.debug("key: {}, value: {}", o, headers.get(o));
        }
    }

    @EventListener
    public void handleWebSocketDisconnectListener(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());
    }

}
