package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Card;
import wannagohome.domain.CardDetailDto;
import wannagohome.domain.ErrorType;
import wannagohome.domain.User;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.CardRepository;
import wannagohome.repository.UserRepository;

import javax.transaction.Transactional;
import java.util.List;

@Service
public class CardService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CardRepository cardRepository;


    public List<Card> findCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalse(user);
    }

    public List<Card> findDueCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(user);
    }

    @Transactional
    public User assignCardToUser(User user, Long cardId, CardDetailDto dto) {
        Card card = cardRepository.findById(cardId).orElseThrow(() -> new NotFoundException(ErrorType.CARD_ID, "없는 카드 아이디 입니다."));
        User assignee = userRepository.findById(dto.getUserId()).orElseThrow(() -> new NotFoundException(ErrorType.USER_ID, "없는 유저 아이디 입니다."));

        card.addAssignee(assignee);
        return assignee;
    }
}
