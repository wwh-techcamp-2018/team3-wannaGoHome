package wannagohome.interceptor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;
import wannagohome.domain.user.SignInDto;
import wannagohome.domain.user.User;
import wannagohome.exception.UnAuthenticationException;
import wannagohome.service.UserService;
import wannagohome.util.SessionUtil;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.nio.charset.Charset;
import java.util.Base64;

public class BasicAuthInterceptor extends HandlerInterceptorAdapter {


    @Autowired
    private UserService userService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String authorization = request.getHeader("Authorization");

        if (authorization == null || !authorization.startsWith("Basic")) {
            return true;
        }

        String base64Credentials = authorization.substring("Basic".length()).trim();
        String credentials = new String(Base64.getDecoder().decode(base64Credentials), Charset.forName("UTF-8"));
        final String[] values = credentials.split(":", 2);

        try {
            User user = userService.signIn(new SignInDto(values[0], values[1]));
            SessionUtil.setUserSession(request.getSession(), user);
        } catch (UnAuthenticationException e) {
            return true;
        }

        return true;
    }
}
