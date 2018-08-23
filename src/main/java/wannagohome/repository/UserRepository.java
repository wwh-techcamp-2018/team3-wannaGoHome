package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAll();
}
