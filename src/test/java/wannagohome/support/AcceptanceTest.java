package wannagohome.support;

import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;
import wannagohome.domain.SignInDto;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("build")
public abstract class AcceptanceTest {

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
