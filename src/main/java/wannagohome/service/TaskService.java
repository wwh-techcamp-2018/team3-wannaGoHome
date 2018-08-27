package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;
import wannagohome.domain.activity.ActivityType;
import wannagohome.domain.activity.CardActivity;
import wannagohome.domain.card.Card;
import wannagohome.domain.card.CardOrderDto;
import wannagohome.domain.error.ErrorType;
import wannagohome.domain.task.Task;
import wannagohome.domain.task.TaskDto;
import wannagohome.domain.user.User;
import wannagohome.event.BoardEvent;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.CardRepository;
import wannagohome.repository.TaskRepository;

import javax.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CardRepository cardRepository;

    @Autowired
    private ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public Task addCard(User user, Long taskId, Card card) {
        Task task = taskRepository.findById(taskId).get();
        task.addCard(card);
        CardActivity activity = CardActivity.valueOf(user, card, ActivityType.CARD_CREATE);
        BoardEvent boardEvent = new BoardEvent(this, activity);
        applicationEventPublisher.publishEvent(boardEvent);
        task = taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task reorderTaskCard(Long taskId, CardOrderDto cardOrderDto) {
        Task task = taskRepository.findById(taskId).get();

        //다른 테스크로 이동할 때
        if(task.getCards().stream().noneMatch(card -> card.equalsId(cardOrderDto.getOriginId()))) {
            Card movingCard =  cardRepository.findById(cardOrderDto.getOriginId())
                    .orElseThrow(()-> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));

            Task beforeTask = movingCard.getTask();
            beforeTask.getCards().remove(movingCard);
            movingCard.setTask(task);
            task.getCards().add(cardOrderDto.getDestinationIndex(), movingCard);

            for(int i = 0; i < beforeTask.getCards().size(); ++i) {
                beforeTask.getCards().get(i).setOrderId(i);
            }
            taskRepository.save(beforeTask);
        } else {
            //같은 테스크 내에서 이동
            Card movingCard = task.getCards().stream()
                    .filter(card -> card.equalsId(cardOrderDto.getOriginId())).findFirst()
                    .orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));
            task.getCards().remove(movingCard);
            task.getCards().add(cardOrderDto.getDestinationIndex(), movingCard);
        }
        for(int i = 0; i < task.getCards().size(); ++i) {
            task.getCards().get(i).setOrderId(i);
        }
        return taskRepository.save(task);
    }

    @Transactional
    public Task renameTask(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId()).get();
        task.setTitle(taskDto.getTitle());
        return taskRepository.save(task);
    }

    @Transactional
    public Task deleteTask(TaskDto taskDto) {
        Task task = taskRepository.findById(taskDto.getId()).get();
        task.setDeleted(true);
        return taskRepository.save(task);
    }
}
