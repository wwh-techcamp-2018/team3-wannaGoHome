package wannagohome.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.context.annotation.PropertySource;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import wannagohome.component.AES256Encoder;
import wannagohome.interceptor.BasicAuthInterceptor;
import wannagohome.interceptor.LoginUserHandlerMethodArgumentResolver;
import wannagohome.interceptor.SignInInterceptor;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

@Configuration
@EnableCaching
@PropertySource("classpath:application.properties")
public class AppConfig implements WebMvcConfigurer {

    @Value("${encoder.key}")
    private String key;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public PasswordEncoder biDirectionEncoder() throws UnsupportedEncodingException {
        return new AES256Encoder(key);
    }

    @Bean
    public LoginUserHandlerMethodArgumentResolver loginUserArgumentResolver() {
        return new LoginUserHandlerMethodArgumentResolver();
    }

    @Bean
    public MessageSource messageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:messages");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor messageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(messageSource);
    }

    @Bean
    public MessageSource activityMessageSource() {
        ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:activity");
        messageSource.setDefaultEncoding("UTF-8");
        messageSource.setCacheSeconds(60);
        return messageSource;
    }

    @Bean
    public MessageSourceAccessor activityMessageSourceAccessor(MessageSource messageSource) {
        return new MessageSourceAccessor(activityMessageSource());
    }

    @Override
    public void addArgumentResolvers(List<HandlerMethodArgumentResolver> argumentResolvers) {
        argumentResolvers.add(loginUserArgumentResolver());
    }

    @Bean
    public SignInInterceptor loginInterceptor() {
        return new SignInInterceptor();
    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(loginInterceptor()).order(1).excludePathPatterns(Arrays.asList(
                "/users/signup",
                "/users/signin",
                "/api/users/signin",
                "/api/users",
                "/js/**",
                "/css/**",
                "/img/**"
        ));
    }

    @Configuration
    @Profile(value = {"dev", "build"})
    @PropertySource("classpath:application.properties")
    class TestConfig extends AppConfig {

        @Value("${encoder.key}")
        private String key;

        @Bean
        public PasswordEncoder biDirectionEncoder() throws UnsupportedEncodingException {
            return new AES256Encoder(key);
        }

        @Bean
        public BasicAuthInterceptor basicAuthInterceptor() {
            return new BasicAuthInterceptor();
        }

        @Override
        public void addInterceptors(InterceptorRegistry registry) {
            super.addInterceptors(registry);
            registry.addInterceptor(basicAuthInterceptor()).order(0);
        }
    }
}
