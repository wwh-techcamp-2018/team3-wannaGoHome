package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.*;
import wannagohome.exception.NotFoundException;
import wannagohome.repository.CardRepository;

import java.util.List;

@Service
public class CardService {

    @Autowired
    private CardRepository cardRepository;


    public List<Card> findCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalse(user);
    }

    public List<Card> findDueCardsByUser(User user) {
        return cardRepository.findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(user);
    }

    public Card setCardDueDate(Long id, CardDetailDto cardDetailDto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));
        card.setEndDate(cardDetailDto.getEndDate());

        return cardRepository.save(card);
    }

    public Card setCardLabel(Long id, CardDetailDto cardDetailDto) {
        Card card = cardRepository.findById(id)
                .orElseThrow(()-> new NotFoundException(ErrorType.CARD_ID, "일치하는 카드가 없습니다."));
        card.setLabels(cardDetailDto.getLabels());

        return cardRepository.save(card);
    }
}
