package wannagohome.service;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.context.ApplicationEventPublisher;
import wannagohome.domain.Card;
import wannagohome.domain.CardOrderDto;
import wannagohome.domain.Task;
import wannagohome.domain.User;
import wannagohome.repository.CardRepository;
import wannagohome.repository.TaskRepository;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(MockitoJUnitRunner.Silent.class)
public class TaskServiceTest {

    @Mock
    private TaskRepository taskRepository;

    @Mock
    private CardRepository cardRepository;

    @Mock
    private ApplicationEventPublisher applicationEventPublisher;

    @InjectMocks
    private TaskService taskService;

    private Task beforeTask;
    private Task afterTask;
    private Card card1;
    private Card card2;
    private Card card3;
    private CardOrderDto cardOrderDto;
    private User user;

    @Before
    public void setUp() throws Exception {
        beforeTask =  beforeTask.builder()
                .title("task title")
                .deleted(false)
                .id(1L)
                .cards(new ArrayList<>())
                .build();
        afterTask = afterTask.builder()
                .title("after task title")
                .deleted(false)
                .id(2L)
                .cards(new ArrayList<>())
                .build();
        card1 = card1.builder()
                .title("card1 title")
                .description("desc")
                .id(1L)
                .task(beforeTask)
                .build();
        card2 = card2.builder()
                .title("card2 title")
                .description("desc")
                .id(2L)
                .build();
        card3 = card3.builder()
                .title("card3 title")
                .description("desc")
                .id(3L)
                .build();

        user = User.builder()
                .password("password")
                .email("yeon@yeon.com")
                .name("yoenfdfadf")
                .build();


        cardOrderDto = cardOrderDto.builder()
                .originId(1L)
                .destinationIndex(0)
                .build();


        when(taskRepository.save(beforeTask)).thenReturn(beforeTask);
        when(taskRepository.save(afterTask)).thenReturn(afterTask);
        when(taskRepository.findById(beforeTask.getId())).thenReturn(Optional.ofNullable(beforeTask));
        when(taskRepository.findById(afterTask.getId())).thenReturn(Optional.ofNullable(afterTask));

    }

    @Test
    public void addCard() {
        taskService.addCard(user, beforeTask.getId(), card2);
        verify(taskRepository, times(1)).save(any());
    }

    @Test
    public void reorderCard_새태스크에카드가없을때() {

        when(cardRepository.findById(cardOrderDto.getOriginId())).thenReturn(Optional.ofNullable(card1));
        taskService.reorderTaskCard(afterTask.getId(), cardOrderDto);

        verify(taskRepository, times(2)).save(any());
    }

    @Test
    public void reorderCard_새태스크에카드가존재할때() {
        taskService.addCard(user, afterTask.getId(), card3);
        when(cardRepository.findById(cardOrderDto.getOriginId())).thenReturn(Optional.ofNullable(card1));

        taskService.reorderTaskCard(afterTask.getId(), cardOrderDto);
        verify(taskRepository, times(3)).save(any());
        assertThat(afterTask.getCards()).containsExactly(card1, card3);
    }

    @Test
    public void reorderCard_같은태스크내에서카드이동할때() {

        taskService.addCard(user, beforeTask.getId(), card3);
        taskService.addCard(user, beforeTask.getId(), card2);
        taskService.addCard(user, beforeTask.getId(), card1);
        when(cardRepository.findById(cardOrderDto.getOriginId())).thenReturn(Optional.ofNullable(card1));
        taskService.reorderTaskCard(beforeTask.getId(), cardOrderDto);
        assertThat(beforeTask.getCards()).containsExactly(card1, card3, card2);
    }
}
