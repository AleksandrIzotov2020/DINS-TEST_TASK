package ru.izotov.userphonebooks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.izotov.userphonebooks.entities.UserEntity;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByUsername(String username);
    List<UserEntity> findByUsernameContaining(String username);
}
