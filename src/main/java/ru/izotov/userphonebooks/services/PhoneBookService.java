package ru.izotov.userphonebooks.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.izotov.userphonebooks.entities.BookEntryEntity;
import ru.izotov.userphonebooks.entities.PhoneBookEntity;
import ru.izotov.userphonebooks.entities.UserEntity;
import ru.izotov.userphonebooks.exceptions.UserInteractionException;
import ru.izotov.userphonebooks.models.PhoneBook;
import ru.izotov.userphonebooks.models.BookEntry;
import ru.izotov.userphonebooks.repositories.PhoneBookRepo;
import ru.izotov.userphonebooks.repositories.BookEntryRepo;
import ru.izotov.userphonebooks.repositories.UserRepo;
import ru.izotov.userphonebooks.services.utils.BookEntryUtils;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class PhoneBookService {

    @Autowired
    private PhoneBookRepo bookRepo;
    @Autowired
    private BookEntryRepo entryRepo;
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private BookEntryUtils numberUtils;



    public boolean editEntry(BookEntryEntity editEntry, Long id) throws UserInteractionException {
        Optional<PhoneBookEntity> book;

        if(!(book = bookRepo.findById(id)).isPresent()){
            return false;
        }
        BookEntryEntity entry = book.get().getEntry();
        String userName = editEntry.getUserName();
        if(userName!= null && !userName.isEmpty()){
            entry.setUserName(userName);
            UserEntity userPhone = entry.getUser();
            if(userPhone!=null)userPhone.setUserName(userName);
        }

        String phoneNumber = editEntry.getPhoneNumber();
        if(phoneNumber != null && !phoneNumber.isEmpty()){
            entry.setPhoneNumber(phoneNumber);
        }

        bookRepo.save(book.get());
        return true;

    }

    public List<PhoneBook> findAllUsersWhoHavePhoneNumberAsEntry(String phoneNumber) throws Exception {
        BookEntryEntity entryEntity = entryRepo
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new UserInteractionException(String.format("Entries with phone number: %s not found", phoneNumber)));

        Map<UserEntity, List<PhoneBookEntity>> map = entryEntity.getBookEntities().stream().collect(Collectors.groupingBy(PhoneBookEntity::getOwner));

        List<PhoneBook> books = new ArrayList<>(map.size());
        for(List book: map.values()){
            books.add(PhoneBook.toModel(book));
        }
        return books;
    }

    public BookEntry findByPhoneNumber(String phoneNumber) throws Exception {
        BookEntryEntity entryEntity = entryRepo
                .findByPhoneNumber(phoneNumber)
                .orElseThrow(()-> new UserInteractionException(String.format("Entries with phone number: %s not found", phoneNumber)));

        return BookEntry.toModel(entryEntity);
    }

    public PhoneBook findById(Long id) throws Exception {
        return PhoneBook
                .toModel(bookRepo
                        .findById(id)
                        .orElseThrow(() -> new UserInteractionException(String.format("No entry with id %s found", id))));
    }

    public Long delete(Long entry_id) throws UserInteractionException {
        bookRepo.findById(entry_id).orElseThrow(()-> new UserInteractionException(String.format("Entry with id %d not found", entry_id)));
        bookRepo.deleteById(entry_id);
        return entry_id;
    }

}
