package wannagohome.repository;

import org.springframework.data.repository.CrudRepository;
import wannagohome.domain.user.User;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {

    Optional<User> findByEmail(String email);

    List<User> findAllByIdNotInAndEmailContainingIgnoreCase(List<Long> userIds, String keyword);
    List<User> findAllByIdNotInAndNameContainingIgnoreCase(List<Long> usersIds, String keyword);

    List<User> findAll();
}
