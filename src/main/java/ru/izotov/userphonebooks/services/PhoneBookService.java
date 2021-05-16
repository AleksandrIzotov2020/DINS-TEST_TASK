package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.phonebook.NoEntriesFoundException;
import ru.izotov.userphonebooks.exceptions.field.UserNotFoundException;
import ru.izotov.userphonebooks.models.PhoneBook;
import ru.izotov.userphonebooks.repositories.PhoneBookRepo;
import ru.izotov.userphonebooks.repositories.PhoneNumberRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class PhoneBookService {

    @Autowired
    private PhoneBookRepo bookRepo;
    @Autowired
    private PhoneNumberRepo numberRepo;
    @Autowired
    private UserRepo userRepo;

    public List<PhoneBook> getAllEntriesForUser(Long user_id) throws UserNotFoundException, NoEntriesFoundException {
        UserEntity userEntity = userRepo
                .findById(user_id)
                .orElseThrow(() -> new UserNotFoundException(String.format("User with id %d not found", user_id)));

        List<PhoneBookEntity> records =  bookRepo.findByUser_id(user_id);

        if(records.isEmpty()){
            throw new NoEntriesFoundException(String.format("User %s has no entries in the phone book", userEntity.getUsername()));
        }

        return records.stream().map(PhoneBook::toModel).collect(Collectors.toList());
    }

    public PhoneBook findById(Long id) throws NoEntriesFoundException {
        return PhoneBook
                .toModel(bookRepo
                        .findById(id)
                        .orElseThrow(() -> new NoEntriesFoundException(String.format("No entry with id %s found", id))));
    }

}
