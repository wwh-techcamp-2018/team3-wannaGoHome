package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.card.Card;
import wannagohome.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface CardRepository extends JpaRepository<Card, Long> {

    Optional<Card> findByIdAndDeletedFalse(Long id);

    List<Card> findAllByAuthorAndDeletedFalse(User user);

    List<Card> findAllByAuthorAndDeletedFalseAndEndDateIsNotNull(User user);
}
