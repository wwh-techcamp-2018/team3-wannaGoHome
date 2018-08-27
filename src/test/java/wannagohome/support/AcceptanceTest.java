package wannagohome.support;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.SignInDto;


public abstract class AcceptanceTest extends SpringTest {

    @Autowired
    private TestRestTemplate template;

    protected TestRestTemplate template() {
        return template;
    }

    protected ResponseEntity basicAuthRequest(RequestEntity requestEntity, SignInDto user) {

        return request(
                template.withBasicAuth(user.getEmail(), user.getPassword()), requestEntity
        );
    }

    protected ResponseEntity request(TestRestTemplate template, RequestEntity requestEntity) {
        return template.exchange(
                requestEntity.getUrl(),
                requestEntity.getMethod(),
                requestEntity.getBody(),
                requestEntity.getReturnType()
        );
    }


}
