package wannagohome.util;

import wannagohome.domain.User;

import javax.servlet.http.HttpSession;

public class SessionUtil {

    private static final String SESSION_KEY = "loginedUser";

    public static void setUserSession(HttpSession session, User user) {
        session.setAttribute(SESSION_KEY, user);
    }

    public static User getUserSession(HttpSession session) {
        User user = (User) session.getAttribute(SESSION_KEY);
        if (user == null)
            return null;

        return user;
    }

    public static void removeUserSession(HttpSession session) {
        session.removeAttribute(SESSION_KEY);
    }
}
