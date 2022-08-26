package reactdemoapp.postgresql.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import reactdemoapp.postgresql.models.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
