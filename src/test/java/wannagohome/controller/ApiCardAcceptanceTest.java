package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.*;
import wannagohome.repository.CardRepository;
import wannagohome.repository.CommentRepository;
import wannagohome.repository.UserRepository;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import javax.xml.ws.Response;
import java.util.Arrays;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiCardAcceptanceTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiCardAcceptanceTest.class);

    private static final String CARD_BASE_URL = "/api/cards/1";

    private static final String COMMENT_BASE_URL = "/api/cards/1/comments";

    private SignInDto signInDto;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private CommentRepository commentRepository;

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

    @Test
    public void comment() {
        CommentDto commentDto = new CommentDto("hello world");

        RequestEntity addCommentRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(commentDto)
                .withReturnType(Comment.class)
                .withUrl(COMMENT_BASE_URL)
                .build();

        ResponseEntity<Comment> addResponse;

        addResponse = basicAuthRequest(addCommentRequest, signInDto);
        Comment comment = addResponse.getBody();

        assertThat(addResponse.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(comment.getContents()).isEqualTo(commentDto.getContents());
        assertThat(comment.getCard().getId()).isEqualTo(1L);
        assertThat(comment.getAuthor().getEmail()).isEqualTo(signInDto.getEmail());

        RequestEntity getCommentRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.GET)
                .withReturnType(Comment[].class)
                .withUrl(COMMENT_BASE_URL)
                .build();

        ResponseEntity<Comment[]> getResponse = basicAuthRequest(getCommentRequest, signInDto);

        assertThat(getResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(getResponse.getBody()).map(Comment::getId)).contains(comment.getId());

        RequestEntity removeCommentRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.DELETE)
                .withReturnType(Comment.class)
                .withUrl(COMMENT_BASE_URL + "/" + comment.getId())
                .build();

        ResponseEntity<Comment> removeResponse = basicAuthRequest(removeCommentRequest, signInDto);
        // TODO: 잘못된 signIn user 의 경우에도 테스트 추가하기
        assertThat(removeResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(commentRepository.findById(comment.getId()).orElseThrow(RuntimeException::new).isDeleted()).isTrue();
    }
}
