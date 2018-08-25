package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
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
        CardDetailDto cardDetailDto = CardDetailDto.builder()
                .userId(1L)
                .build();

        RequestEntity assignRequest = new RequestEntity.Builder()
                .withReturnType(User.class)
                .withMethod(HttpMethod.POST)
                .withUrl(CARD_BASE_URL + "/assign")
                .withBody(cardDetailDto)
                .build();

        ResponseEntity<User> responseEntity = basicAuthRequest(assignRequest, signInDto);

        User actual = userRepository.findByEmail(signInDto.getEmail()).orElseThrow(RuntimeException::new);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody()).isEqualTo(actual);

        Card card = cardRepository.findById(1L).orElseThrow(RuntimeException::new);
        assertThat(card.getAssignees()).contains(actual);
    }
}
