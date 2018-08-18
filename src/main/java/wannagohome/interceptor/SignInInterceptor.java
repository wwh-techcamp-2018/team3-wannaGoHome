package wannagohome.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wannagohome.domain.User;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class SignInInterceptor extends HandlerInterceptorAdapter {
    private static final Logger log = LoggerFactory.getLogger(SignInInterceptor.class);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.debug("request url: {}", request.getRequestURL().toString());
        User user = SessionUtil.getUserSession(request.getSession());
//        if (user.isGuestUser()) {
//            response.sendRedirect("/users/signin");
//            return false;
//        }
        return true;
    }
}
