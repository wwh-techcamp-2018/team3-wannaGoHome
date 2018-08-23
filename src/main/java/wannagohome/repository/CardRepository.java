package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.Card;
import wannagohome.domain.User;

import java.util.List;

public interface CardRepository extends JpaRepository<Card, Long> {

    List<Card> findAllByAuthorAndDeletedFalse(User user);

    List<Card> findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(User user);
}
