package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.Task;

public interface TaskRepository extends CrudRepository<Task, Long> {
    Task findByIdEquals(Long id);
}
