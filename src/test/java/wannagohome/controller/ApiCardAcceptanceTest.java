package wannagohome.controller;

import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import wannagohome.domain.card.*;
import wannagohome.domain.user.SignInDto;
import wannagohome.domain.user.User;
import wannagohome.repository.CardRepository;
import wannagohome.repository.CommentRepository;
import wannagohome.repository.UserRepository;
import wannagohome.support.AcceptanceTest;
import wannagohome.support.RequestEntity;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.stream.Collectors;

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
                .withReturnType(AssigneeDto[].class)
                .withMethod(HttpMethod.POST)
                .withUrl(CARD_BASE_URL + "/assign")
                .withBody(cardDetailDto)
                .build();

        ResponseEntity<AssigneeDto[]> responseEntity;
        Card card;

        responseEntity = basicAuthRequest(assignRequest, signInDto);

        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);

        card = cardRepository.findById(1L).orElseThrow(RuntimeException::new);
        log.debug("after assignCardToUser: {}", card.getAssignees());
        assertThat(card.getAssignees()).contains(actual);

        RequestEntity dischargeEntity = new RequestEntity.Builder()
                .withReturnType(AssigneeDto[].class)
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

    @Test
    public void setDueDate() {
        Date date = new Date();
        CardDetailDto cardDetailDto = CardDetailDto.builder()
                .id(1L)
                .endDate(date)
                .build();
        RequestEntity setDueDateRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(cardDetailDto)
                .withReturnType(Card.class)
                .withUrl("/api/cards/1/date")
                .build();

        ResponseEntity<Card> responseEntity = basicAuthRequest(setDueDateRequest, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(responseEntity.getBody().getEndDate()).isEqualTo(date);
    }

    @Test
    public void updateCardDate() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = dateFormat.parse("2018-08-20");
        Date endDate = dateFormat.parse("2018-08-30");
        CardDetailDto cardDetailDto = CardDetailDto.builder()
                .id(1L)
                .createDate(startDate)
                .endDate(endDate)
                .build();

        RequestEntity updateDueDateRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.PUT)
                .withBody(cardDetailDto)
                .withReturnType(Card.class)
                .withUrl("/api/cards/1/date")
                .build();
        ResponseEntity<Card> responseEntity = basicAuthRequest(updateDueDateRequest, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(responseEntity.getBody().getEndDate()).isEqualTo(endDate);
        assertThat(responseEntity.getBody().getCreateDate()).isEqualTo(startDate);
    }

    @Test
    public void add_label() {
        Label label = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();

        RequestEntity addLabelRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(label)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();

        ResponseEntity<CardLabelDto[]> responseEntity = basicAuthRequest(addLabelRequest, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(Arrays.stream(responseEntity.getBody())
                .filter(cardLabelDto -> cardLabelDto.isChecked()).collect(Collectors.toList()).size()).isEqualTo(1);

        Card card = cardRepository.findById(1L).get();
        card.getLabels().clear();
        cardRepository.save(card);

    }

    @Test
    public void add_delete_label() {
        Label label = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();
        RequestEntity addLabelRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(label)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        ResponseEntity<CardLabelDto[]> responseEntity = basicAuthRequest(addLabelRequest, signInDto);
        assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        RequestEntity deleteLabelRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.DELETE)
                .withBody(label)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        ResponseEntity<CardLabelDto[]> deleteResponseEntity = basicAuthRequest(deleteLabelRequest, signInDto);
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(deleteResponseEntity.getBody())
                .filter(cardLabelDto -> cardLabelDto.isChecked()).collect(Collectors.toList()).size()).isEqualTo(0);

        Card card = cardRepository.findById(1L).get();
        card.getLabels().clear();
        cardRepository.save(card);

    }

    @Test
    public void add_delete_labels() {
        Label label1 = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();

        Label label2 = Label.builder()
                .id(2L)
                .color(LabelColor.ORANGE)
                .build();
        RequestEntity addLabel1Request = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(label1)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        RequestEntity addLabel2Request = new RequestEntity.Builder()
                .withMethod(HttpMethod.POST)
                .withBody(label2)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        ResponseEntity<CardLabelDto[]> addLabel1responseEntity = basicAuthRequest(addLabel1Request, signInDto);
        assertThat(addLabel1responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        ResponseEntity<CardLabelDto[]> addLabel2responseEntity = basicAuthRequest(addLabel2Request, signInDto);
        assertThat(addLabel2responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);

        assertThat(Arrays.stream(addLabel2responseEntity.getBody())
                .filter(cardLabelDto -> cardLabelDto.isChecked()).collect(Collectors.toList()).size()).isEqualTo(2);

        RequestEntity deleteLabelRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.DELETE)
                .withBody(label1)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        ResponseEntity<CardLabelDto[]> deleteResponseEntity = basicAuthRequest(deleteLabelRequest, signInDto);
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(Arrays.stream(deleteResponseEntity.getBody())
                .filter(cardLabelDto -> cardLabelDto.isChecked()).collect(Collectors.toList()).size()).isEqualTo(1);
        Card card = cardRepository.findById(1L).get();
        card.getLabels().clear();
        cardRepository.save(card);

    }

    @Test
    public void delete_label_존재안하는라벨지울때() {
        Label label = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();

        RequestEntity deleteLabelRequest = new RequestEntity.Builder()
                .withMethod(HttpMethod.DELETE)
                .withBody(label)
                .withReturnType(CardLabelDto[].class)
                .withUrl("/api/cards/1/label")
                .build();
        ResponseEntity<CardLabelDto[]> deleteResponseEntity = basicAuthRequest(deleteLabelRequest, signInDto);
        assertThat(deleteResponseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
    }



}
