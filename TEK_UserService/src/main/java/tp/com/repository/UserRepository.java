package tp.com.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import tp.com.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}