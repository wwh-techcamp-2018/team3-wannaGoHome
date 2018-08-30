package wannagohome.util;

import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import wannagohome.domain.board.Board;
import wannagohome.domain.card.Card;
import wannagohome.domain.task.Task;
import wannagohome.domain.user.User;
import wannagohome.interceptor.HttpHandshakeInterceptor;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.Objects;
import java.util.Optional;

public class SessionUtil {
    private static final String SESSION_KEY = "loginedUser";

    // Testing purpose
    private static final String BOARD_SESSION_KEY = "viewerBoard";

    public static void setUserSession(HttpSession session, User user) {
        session.setAttribute(SESSION_KEY, user);
    }


    public static User getUserFromWebRequest(NativeWebRequest request) {
        return Optional.ofNullable((User) request.getAttribute(SESSION_KEY, WebRequest.SCOPE_SESSION))
                .orElse(User.GUEST_USER);
    }

    public static User getUserSession(HttpSession session) {
        return Optional.ofNullable((User) session.getAttribute(SESSION_KEY))
                .orElse(User.GUEST_USER);
    }

    public static boolean isLogined(HttpSession session) {
        return !Objects.isNull(session.getAttribute(SESSION_KEY));
    }

    public static User getUserSession(SimpMessageHeaderAccessor headerAccessor) {
        HttpSession session = (HttpSession) headerAccessor.getSessionAttributes().get(HttpHandshakeInterceptor.SESSION);
        return getUserSession(session);
    }

    public static void removeUserSession(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }

    public static void setBoardInSession(HttpSession session) {
        Board dummyBoard = new Board();
        dummyBoard.setTitle("Test Board");
        dummyBoard.setTasks(new ArrayList<Task>());
        dummyBoard.getTasks().add(new Task("Task number 1", new ArrayList<Card>()));
        dummyBoard.getTasks().add(new Task("Task number 2", new ArrayList<Card>()));

        session.setAttribute(BOARD_SESSION_KEY, dummyBoard);
    }

    public static Board getBoardInSession(HttpSession session) {
        if (session.getAttribute(BOARD_SESSION_KEY) == null) SessionUtil.setBoardInSession(session);
        return (Board) session.getAttribute(BOARD_SESSION_KEY);
    }
}
