package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.repository.CardRepository;
import wannagohome.repository.TaskRepository;

import javax.transaction.Transactional;

@Service
public class TaskService {

    @Autowired
    private TaskRepository taskRepository;

    @Autowired
    private CardRepository cardRepository;

    public Task addCard(Long taskId, Card card) {
        Task task = taskRepository.findById(taskId).get();
        task.addCard(card);
        task = taskRepository.save(task);
        return task;
    }

    @Transactional
    public Task reorderTaskCard(Long taskId, CardOrderDto cardOrderDto) {
        Task task = taskRepository.findById(taskId).get();
//        task.reorderCard(cardOrderDto);

        for(int i = 0; i < task.getCards().size(); ++i) {
            if(task.getCards().get(i).equalsId(cardOrderDto.getOriginId())) {
                Card movingCard = task.getCards().get(i);
                task.getCards().remove(i);
                task.getCards().add(cardOrderDto.getDestinationIndex(), movingCard);
                break;
            }
        }
        //다른 테스크로 이동할 때
        if(task.getCards().stream().noneMatch(card -> card.equalsId(cardOrderDto.getOriginId()))) {

            Card movingCard =  cardRepository.findById(cardOrderDto.getOriginId()).get();
            Task beforeTask = movingCard.getTask();
            for (int i = 0; i < beforeTask.getCards().size(); ++i) {
                if(beforeTask.getCards().get(i).equalsId(cardOrderDto.getOriginId())) {
                    beforeTask.getCards().remove(i);
                    break;
                }
            }
            movingCard.setTask(task);

//            if(cardOrderDto.getDestinationIndex() >= task.getCards().size()) {
//
//            }
            task.getCards().add(cardOrderDto.getDestinationIndex(), movingCard);
            for(int i = 0; i < beforeTask.getCards().size(); ++i) {
                beforeTask.getCards().get(i).setOrderId(i);
            }
            taskRepository.save(beforeTask);
        }
        for(int i = 0; i < task.getCards().size(); ++i) {
            task.getCards().get(i).setOrderId(i);
        }
        return taskRepository.save(task);
    }
}
