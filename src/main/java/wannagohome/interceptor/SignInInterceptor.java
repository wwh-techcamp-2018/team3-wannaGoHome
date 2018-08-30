package wannagohome.interceptor;

import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wannagohome.domain.user.User;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignInInterceptor extends HandlerInterceptorAdapter {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        User user = SessionUtil.getUserSession(request.getSession());
        if (user.isGuestUser()) {
            response.sendRedirect("/users/signin");
            return false;
        }
        return true;
    }
}
