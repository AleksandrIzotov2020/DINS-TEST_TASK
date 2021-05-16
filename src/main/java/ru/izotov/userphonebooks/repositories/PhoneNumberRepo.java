package ru.izotov.userphonebooks.repositories;

import org.springframework.data.repository.CrudRepository;
import ru.izotov.userphonebooks.entities.PhoneNumberEntity;

import java.util.Optional;

public interface PhoneNumberRepo extends CrudRepository<PhoneNumberEntity, Long> {
    Optional<PhoneNumberEntity> findByPhonenumber(String phonenumber);
}
