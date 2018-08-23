package wannagohome.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import wannagohome.domain.Card;
import wannagohome.domain.User;
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
}
