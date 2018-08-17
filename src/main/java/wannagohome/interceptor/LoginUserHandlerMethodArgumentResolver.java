package wannagohome.interceptor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;
import wannagohome.domain.ErrorType;
import wannagohome.domain.User;
import wannagohome.exception.UnAuthenticationException;
import wannagohome.util.SessionUtil;

public class LoginUserHandlerMethodArgumentResolver implements HandlerMethodArgumentResolver {
    private static final Logger log = LoggerFactory.getLogger(LoginUserHandlerMethodArgumentResolver.class);

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(LoginUser.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, ModelAndViewContainer mavContainer, NativeWebRequest webRequest, WebDataBinderFactory binderFactory) throws Exception {
        User user = SessionUtil.getUserFromWebRequest(webRequest);
        log.debug("username: {}", user.getName());
        if (!user.isGuestUser()) {
            return user;
        }

        LoginUser loginUser = parameter.getParameterAnnotation(LoginUser.class);
        if (loginUser.required()) {
            throw new UnAuthenticationException(ErrorType.UNAUTHENTICATED, "로그인이 필요한 서비스입니다.");
        }
        return user;
    }
}
