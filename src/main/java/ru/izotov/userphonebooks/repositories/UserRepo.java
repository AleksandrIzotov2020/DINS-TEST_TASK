package ru.izotov.userphonebooks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.izotov.userphonebooks.entities.UserEntity;

import java.util.List;

public interface UserRepo extends CrudRepository<UserEntity, Long> {
    UserEntity findByUserName(String username);
    List<UserEntity> findByUserNameContaining(String username);
}
