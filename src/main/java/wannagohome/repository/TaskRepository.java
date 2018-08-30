package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.task.Task;

import java.util.Optional;

public interface TaskRepository extends CrudRepository<Task, Long> {

    Optional<Task> findByIdAndDeletedFalse(Long id);
}
