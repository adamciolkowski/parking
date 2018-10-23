package pl.touk.parking.repository;

import org.springframework.data.repository.Repository;
import pl.touk.parking.domain.User;

import java.util.Optional;

public interface UserRepository extends Repository<User, Integer> {

    Optional<User> findByUsername(String username);
}
