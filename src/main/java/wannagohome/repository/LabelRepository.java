package wannagohome.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import wannagohome.domain.Label;

public interface LabelRepository extends JpaRepository<Label, Long> {
}
