package ru.izotov.userphonebooks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;

import java.util.List;
import java.util.Optional;

public interface PhoneBookRepo extends CrudRepository<PhoneBookEntity, Long> {
    List<PhoneBookEntity> findByUser_id(Long user_id);
}
