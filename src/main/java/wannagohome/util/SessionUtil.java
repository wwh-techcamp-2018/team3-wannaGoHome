package wannagohome.util;

import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.context.request.WebRequest;
import wannagohome.domain.User;

import javax.servlet.http.HttpSession;
import java.util.Optional;

public class SessionUtil {

    private static final String SESSION_KEY = "loginedUser";

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

    public static void removeUserSession(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}
