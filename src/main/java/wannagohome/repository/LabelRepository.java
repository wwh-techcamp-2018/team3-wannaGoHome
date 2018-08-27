package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.card.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
