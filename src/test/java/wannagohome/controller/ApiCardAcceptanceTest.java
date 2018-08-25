package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.Card;
import wannagohome.domain.CardDetailDto;
import wannagohome.domain.SignInDto;
import wannagohome.domain.User;
import wannagohome.repository.CardRepository;
import wannagohome.repository.UserRepository;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiCardAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiCardAcceptanceTest.class);

    private static final String CARD_BASE_URL = "/api/cards/1";

    private SignInDto signInDto;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Before
    public void setUp() throws Exception {
        signInDto = new SignInDto("junsulime@woowahan.com", "password1");
    }

    @Test
    public void assignCardToUser() {
        User actual = userRepository.findByEmail(signInDto.getEmail()).orElseThrow(RuntimeException::new);

        CardDetailDto cardDetailDto = CardDetailDto.builder()
                .userId(actual.getId())
                .build();

        RequestEntity assignRequest = new RequestEntity.Builder()
                .withReturnType(User.class)
                .withMethod(HttpMethod.POST)
                .withUrl(CARD_BASE_URL + "/assign")
                .withBody(cardDetailDto)
                .build();

        ResponseEntity<User> responseEntity;
        Card card;

        responseEntity = basicAuthRequest(assignRequest, signInDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(actual);

        card = cardRepository.findById(1L).orElseThrow(RuntimeException::new);
        log.debug("after assignCardToUser: {}", card.getAssignees());
        assertThat(card.getAssignees()).contains(actual);

        RequestEntity dischargeEntity = new RequestEntity.Builder()
                .withReturnType(User.class)
                .withMethod(HttpMethod.DELETE)
                .withUrl(CARD_BASE_URL + "/assign")
                .withBody(cardDetailDto)
                .build();

        responseEntity = basicAuthRequest(dischargeEntity, signInDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        card = cardRepository.findById(1L).orElseThrow(RuntimeException::new);
        log.debug("after dischargeCardFromUser: {}", card.getAssignees());
        assertThat(card.getAssignees()).doesNotContain(actual);
    }
}
