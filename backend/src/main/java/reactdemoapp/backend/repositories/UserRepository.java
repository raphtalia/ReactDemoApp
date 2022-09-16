package reactdemoapp.backend.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import reactdemoapp.backend.models.User;

public interface UserRepository extends JpaRepository<User, Integer> {
    User findByUserId(long userId);
    User findByEmail(String email);
//    User findByEmailAndPassword(String email, String password);
}
