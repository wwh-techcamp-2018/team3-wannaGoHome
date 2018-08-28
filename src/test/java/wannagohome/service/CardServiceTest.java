package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import wannagohome.domain.card.*;
import wannagohome.domain.user.User;
import wannagohome.repository.CardRepository;
import wannagohome.repository.LabelRepository;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CardServiceTest {

    @Mock
    private CardRepository cardRepository;

    @Mock
    private LabelRepository labelRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private CardService cardService;

    private CardDetailDto cardDetailDto;

    private CardDetailDto updateCardDetailDto;

    private Card card;

    private Label label;

    private User source;

    @Before
    public void setUp() throws Exception {
        source = User.builder()
                .email("yeon@woowahan.com")
                .name("jhyang")
                .password("kookoointae")
                .build();

        label = Label.builder()
                .id(1L)
                .color(LabelColor.RED)
                .build();

        List<Label> labelList = new ArrayList<>();
        labelList.add(label);

        DateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd");
        Date startDate = dateFormat.parse("2018-08-20");
        Date endDate = dateFormat.parse("2018-08-30");
        cardDetailDto = CardDetailDto.builder()
                .id(1L)
                .createDate(startDate)
                .endDate(endDate)
                .labels(labelList)
                .build();

        Date updateStartDate = dateFormat.parse("2018-08-22");
        Date updateEndDate = dateFormat.parse("2018-08-31");
        updateCardDetailDto = CardDetailDto.builder()
                .id(1L)
                .createDate(updateStartDate)
                .endDate(updateEndDate)
                .labels(labelList)
                .build();

        card = Card.builder()
                .id(1L)
                .assignees(new ArrayList<>())
                .description("hello")
                .labels(new ArrayList<>())
                .build();

        when(cardRepository.findById(card.getId())).thenReturn(Optional.ofNullable(card));
        when(cardRepository.save(card)).thenReturn(card);
        when(labelRepository.findById(label.getId())).thenReturn(Optional.ofNullable(label));
    }

    @Test
    public void setCardDueDate() {
        cardService.setCardDueDate(source, card.getId(), cardDetailDto);
        verify(cardRepository, times(1)).save(any());
        verify(cardRepository, times(1)).findById((any()));
        assertThat(card.getEndDate()).isEqualTo(cardDetailDto.getEndDate());
    }

    @Test
    public void setCardLabel() {
        cardService.setCardLabel(source, card.getId(), cardDetailDto);
        verify(cardRepository, times(1)).save(any());
        verify(cardRepository, times(1)).findById((any()));
        assertThat(card.getLabels()).contains(label);
    }

    @Test
    public void addLabel() {
        List<Label> labelList = Arrays.asList(
                Label.builder()
                        .id(1L)
                        .color(LabelColor.RED)
                        .build(),
                Label.builder()
                        .id(2L)
                        .color(LabelColor.ORANGE)
                        .build(),
                Label.builder()
                        .id(3L)
                        .color(LabelColor.YELLOW)
                        .build(),
                Label.builder()
                        .id(4L)
                        .color(LabelColor.GREEN)
                        .build(),
                Label.builder()
                        .id(5L)
                        .color(LabelColor.BLUE)
                        .build()
        );
        cardService.addLabel(source, card.getId(), label);
        verify(cardRepository, times(1)).save(any());
        verify(cardRepository, times(1)).findById((any()));
        assertThat(card.getLabels()).contains(label);
    }

    @Test
    public void getLabels() {
        List<Label> labelList = Arrays.asList(
                Label.builder()
                        .id(1L)
                        .color(LabelColor.RED)
                        .build(),
                Label.builder()
                        .id(2L)
                        .color(LabelColor.ORANGE)
                        .build(),
                Label.builder()
                        .id(3L)
                        .color(LabelColor.YELLOW)
                        .build(),
                Label.builder()
                        .id(4L)
                        .color(LabelColor.GREEN)
                        .build(),
                Label.builder()
                        .id(5L)
                        .color(LabelColor.BLUE)
                        .build()
        );
        cardService.addLabel(source, card.getId(), label);
        List<Label> labels = cardService.getLabels(card.getId());
        assertThat(labels.size()).isEqualTo(1);
        verify(cardRepository, times(1)).save(any());
        verify(cardRepository, times(2)).findById((any()));
    }

    @Test
    public void add_deleteLabel() {
        when(labelRepository.findById(label.getId())).thenReturn(Optional.ofNullable(label));
        cardService.addLabel(source, card.getId(), label);
        cardService.deleteLabel(card.getId(), label.getId());
        List<Label> labels = cardService.getLabels(card.getId());
        assertThat(labels.size()).isEqualTo(0);
        verify(labelRepository, times(1)).findById((any()));
    }

    @Test
    public void updateDate() {
        cardService.setCardDueDate(source, card.getId(), cardDetailDto);
        assertThat(card.getEndDate()).isEqualTo(cardDetailDto.getEndDate());

        cardService.updateCardDate(source, card.getId(), updateCardDetailDto);
        assertThat(card.getEndDate()).isEqualTo(updateCardDetailDto.getEndDate());
        assertThat(card.getCreateDate()).isEqualTo(updateCardDetailDto.getCreateDate());

        verify(cardRepository, times(2)).save(any());
        verify(cardRepository, times(2)).findById((any()));
    }





}
