package ru.izotov.userphonebooks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.izotov.userphonebooks.entities.BookEntryEntity;

import java.util.Optional;

public interface BookEntryRepo extends CrudRepository<BookEntryEntity, Long> {
    Optional<BookEntryEntity> findByPhoneNumber(String phoneNumber);
    Optional<BookEntryEntity> findByUserName(String userName);
}
